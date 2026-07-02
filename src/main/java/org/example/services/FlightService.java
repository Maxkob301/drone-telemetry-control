package org.example.services;


import org.example.model.Telemetry;
import org.example.model.entities.FlightRecord;
import org.example.model.repository.FlightRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class FlightService {

    private final FlightRecordRepository flightRecordRepository;


    @Autowired
    public FlightService(FlightRecordRepository flightRecordRepository) {
        this.flightRecordRepository = flightRecordRepository;
    }

    public void save(FlightRecord record){
        flightRecordRepository.save(record);
    }


    public List<FlightRecord> findAll(){
        return flightRecordRepository.findAll();
    }

    public List<FlightRecord> findByDate(LocalDate flightDate){
        return flightRecordRepository.findByFlightDate(flightDate);
    }

    public void saveFromTelemetry(Telemetry telemetry) {
        FlightRecord record = FlightRecord.builder()
                .latitude(telemetry.getLatitude())
                .longitude(telemetry.getLongitude())
                .altitude(telemetry.getAltitude())
                .relativeAltitude(telemetry.getRelativeAltitude())
                .groundSpeed(telemetry.getGroundSpeed())
                .heading(telemetry.getHeading())
                .flightDate(Instant.ofEpochMilli(telemetry.getTimestamp())
                                .atZone(ZoneOffset.UTC)
                                .toLocalDate()).build();
        flightRecordRepository.save(record);
    }

}
