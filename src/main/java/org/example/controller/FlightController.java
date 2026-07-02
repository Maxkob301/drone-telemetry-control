package org.example.controller;

import org.example.model.entities.FlightRecord;
import org.example.services.FlightService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    public List<FlightRecord> getFlights(@RequestParam(required = false) String date) {
        if (date != null) {
            LocalDate flightDate = LocalDate.parse(date);
            return flightService.findByDate(flightDate);
        }
        return flightService.findAll();
    }
}