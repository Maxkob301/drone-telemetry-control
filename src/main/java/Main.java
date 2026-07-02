

import org.example.mavlink.MavlinkConnector;
import org.example.model.Telemetry;
import org.example.services.FlightService;
import org.example.websocket.TelemetryWebSocketHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Main implements CommandLineRunner {

    private final FlightService flightService;
    private final TelemetryWebSocketHandler webSocketHandler;
    private static Process simulatorProcess;
    private final MavlinkConnector mavlinkConnector;

    public Main(FlightService flightService, TelemetryWebSocketHandler webSocketHandler, MavlinkConnector mavlinkConnector) {
        this.flightService = flightService;
        this.webSocketHandler = webSocketHandler;
        this.mavlinkConnector = mavlinkConnector;
    }

    @Override
    public void run(String... args) throws Exception {
        startXServer();
        setupDisplay();
        startSimulator();



        try {
            mavlinkConnector.connect();
            System.out.println("Подключено к симулятору!");

            Thread.sleep(2000);
            mavlinkConnector.sendTakeoff(5);

            while (true) {
                Telemetry telemetry = mavlinkConnector.getLastTelemetry();
                if (telemetry != null) {
                    System.out.println(telemetry);
                }
                Thread.sleep(100);
            }
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        } finally {
            mavlinkConnector.close();
            stopSimulator();
        }
    }

    private static void startSimulator() {
        try {
            String px4Path = "/home/hushdh/PX4-Autopilot";
            ProcessBuilder pb = new ProcessBuilder(
                    "bash", "-c",
                    "cd " + px4Path + " && make px4_sitl jmavsim"
            );
            pb.inheritIO();
            simulatorProcess = pb.start();
            System.out.println("Симулятор запускается, ждём 15 секунд...");
            Thread.sleep(15000);
        } catch (Exception e) {
            System.err.println("Не удалось запустить симулятор: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void stopSimulator() {
        if (simulatorProcess != null && simulatorProcess.isAlive()) {
            simulatorProcess.destroy();
            System.out.println("Симулятор остановлен.");
        }
    }

    private static void startXServer() {
        try {
            String vcxsrvPath = "C:\\Program Files\\VcXsrv\\vcxsrv.exe";
            ProcessBuilder pb = new ProcessBuilder(
                    vcxsrvPath,
                    ":0",
                    "-multiwindow",
                    "-clipboard",
                    "-ac"
            );
            pb.inheritIO();
            pb.start();
            System.out.println("X-сервер запущен!");
            Thread.sleep(2000);
        } catch (Exception e) {
            System.err.println("Не удалось запустить X-сервер: " + e.getMessage());
        }
    }

    private static void setupDisplay() {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "bash", "-c",
                    "export DISPLAY=localhost:0.0 && echo $DISPLAY"
            );
            pb.inheritIO();
            Process process = pb.start();
            process.waitFor();
            System.out.println("DISPLAY установлен в localhost:0.0");
        } catch (Exception e) {
            System.err.println("Не удалось установить DISPLAY: " + e.getMessage());
        }
    }
}