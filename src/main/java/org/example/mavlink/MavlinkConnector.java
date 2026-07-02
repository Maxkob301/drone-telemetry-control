package org.example.mavlink;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dronefleet.mavlink.MavlinkConnection;
import io.dronefleet.mavlink.MavlinkMessage;
import io.dronefleet.mavlink.common.CommandLong;
import io.dronefleet.mavlink.common.GlobalPositionInt;
import io.dronefleet.mavlink.common.MavCmd;
import io.dronefleet.mavlink.minimal.Heartbeat;
import org.example.model.Telemetry;
import org.example.services.FlightService;
import org.example.websocket.TelemetryWebSocketHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


@Component
public class MavlinkConnector implements AutoCloseable {

    private MavlinkConnection connection;
    private DatagramSocket socket;
    private volatile Telemetry lastTelemetry;
    private volatile boolean running = true;
    private final TelemetryWebSocketHandler webSocketHandler;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final FlightService flightService;

    public MavlinkConnector(FlightService flightService, TelemetryWebSocketHandler webSocketHandler) {
        this.flightService = flightService;
        this.webSocketHandler = webSocketHandler;
    }



    public void connect() throws Exception {
        socket = new DatagramSocket(14550);
        InetAddress remoteAddress = InetAddress.getByName("127.0.0.1");

        // Создание пайпов для связи UDP и MAVLink
        PipedOutputStream toMavlinkOut = new PipedOutputStream();
        PipedInputStream fromUdpIn = new PipedInputStream(toMavlinkOut);

        PipedOutputStream toUdpOut = new PipedOutputStream();
        Thread sender = getThread(toUdpOut, toMavlinkOut, remoteAddress);
        sender.start();

        // Создание MAVLink-соединения
        connection = MavlinkConnection.create(fromUdpIn, toUdpOut);

        // Запуск потока для чтения и обработки сообщений
        Thread readerThread = new Thread(() -> {
            while (running && !socket.isClosed()) {
                try {
                    MavlinkMessage<?> msg = connection.next();
                    if (msg != null) {
                        processMessage(msg);
                    }
                } catch (Exception e) {
                    if (running) {
                        e.printStackTrace();
                    }
                }
            }
        });
        readerThread.setDaemon(true);
        readerThread.start();

        System.out.println("MavlinkConnector: подключен к симулятору!");
    }

    private Thread getThread(PipedOutputStream toUdpOut, PipedOutputStream toMavlinkOut, InetAddress remoteAddress) throws IOException {
        PipedInputStream fromMavlinkIn = new PipedInputStream(toUdpOut);

        // Поток для приёма данных (UDP → MAVLink)
        Thread receiver = new Thread(() -> {
            byte[] buffer = new byte[65535];
            while (!socket.isClosed()) {
                try {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    toMavlinkOut.write(packet.getData(), packet.getOffset(), packet.getLength());
                } catch (Exception e) {
                    if (!socket.isClosed()) {
                        e.printStackTrace();
                    }
                }
            }
        });
        receiver.setDaemon(true);
        receiver.start();

        // Поток для отправки данных (MAVLink → UDP)
        Thread sender = new Thread(() -> {
            byte[] buffer = new byte[2048];
            while (!socket.isClosed()) {
                try {
                    int len = fromMavlinkIn.read(buffer);
                    if (len > 0) {
                        DatagramPacket packet = new DatagramPacket(buffer, len, remoteAddress, 14550);
                        socket.send(packet);
                    }
                } catch (Exception e) {
                    if (!socket.isClosed()) {
                        e.printStackTrace();
                    }
                }
            }
        });
        sender.setDaemon(true);
        return sender;
    }

    private void processMessage(MavlinkMessage<?> msg) {
        Object payload = msg.getPayload();

        if (payload instanceof Heartbeat) {
            Heartbeat hb = (Heartbeat) payload;
            System.out.println("Heartbeat: " + hb.type());
        }

        if (payload instanceof GlobalPositionInt) {
            GlobalPositionInt pos = (GlobalPositionInt) payload;
            double lat = pos.lat() / 1e7;
            double lon = pos.lon() / 1e7;
            double alt = pos.alt() / 1000.0;
            double relativeAlt = pos.relativeAlt() / 1000.0;
            lastTelemetry = new Telemetry(lat, lon, alt, relativeAlt,
                    0.0, // groundSpeed пока нет
                    0.0, // heading пока нет
                    System.currentTimeMillis());

            // Отправляем через WebSocket
            try {
                String json = objectMapper.writeValueAsString(lastTelemetry);
                webSocketHandler.sendTelemetry(json);
                flightService.saveFromTelemetry(lastTelemetry);
            } catch (Exception e) {
                System.err.println("Ошибка сериализации: " + e.getMessage());
            }
        }
    }

    public Telemetry getLastTelemetry() {
        return lastTelemetry;
    }

    public boolean isConnected() {
        return connection != null && socket != null && !socket.isClosed();
    }

    private void sendCommand(CommandLong command) throws Exception {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected to the simulator");
        }
        // send1(systemId, componentId, payload)
        connection.send1(1, 0, command);
    }

    // Взлёт
    public void sendTakeoff(float altitude) throws Exception {
        CommandLong takeoff = CommandLong.builder()
                .targetSystem(1)
                .targetComponent(1)
                .command(MavCmd.MAV_CMD_NAV_TAKEOFF)
                .param7(altitude)
                .build();
        sendCommand(takeoff);
        System.out.println("Команда взлёта отправлена! Высота: " + altitude + " м");
    }

    // Посадка
    public void sendLand() throws Exception {
        CommandLong land = CommandLong.builder()
                .targetSystem(1)
                .targetComponent(1)
                .command(MavCmd.MAV_CMD_NAV_LAND)
                .build();
        sendCommand(land);
        System.out.println("Команда посадки отправлена!");
    }

    @Override
    public void close() {
        running = false;
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        System.out.println("MavlinkConnector: ресурсы закрыты.");
    }
}