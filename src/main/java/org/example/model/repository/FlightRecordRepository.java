package org.example.model.repository;

import org.example.model.entities.FlightRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FlightRecordRepository extends JpaRepository<FlightRecord, Long> {
    List<FlightRecord> findByFlightDate(LocalDate date);
}