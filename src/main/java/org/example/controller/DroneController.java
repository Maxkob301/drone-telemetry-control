package org.example.controller;


import org.example.mavlink.MavlinkConnector;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/drone")
public class DroneController {

    private final MavlinkConnector mavlinkConnector;

    public DroneController(MavlinkConnector mavlinkConnector) {
        this.mavlinkConnector = mavlinkConnector;
    }
    @PostMapping("/takeoff")
    public ResponseEntity<String> takeOff(){
        try {
            mavlinkConnector.sendTakeoff(5);
            return ResponseEntity.ok("Takeoff command sent");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error: " + e.getMessage());
        }
    }
    @PostMapping("/land")
    public ResponseEntity<String> land(){
        try {
            mavlinkConnector.sendLand();
            return ResponseEntity.ok("Land command sent");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error: " + e.getMessage());
        }
    }
}
