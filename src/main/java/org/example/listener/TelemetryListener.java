package org.example.listener;

import org.example.model.Telemetry;

public interface TelemetryListener {

    //метод вызывается, когда приходит новый пакет телеметрии.
    void onTelemetryUpdate(Telemetry telemetry);

}
