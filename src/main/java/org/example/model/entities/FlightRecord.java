package org.example.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "flight_records")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double latitude;
    private double longitude;
    private double altitude;
    private double relativeAltitude;
    private double groundSpeed;
    private double heading;

    @Column(name = "flight_date")
    private LocalDate flightDate;


    public Long getId() { return id; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public double getAltitude() { return altitude; }
    public double getRelativeAltitude() { return relativeAltitude; }
    public double getGroundSpeed() { return groundSpeed; }
    public double getHeading() { return heading; }
    public LocalDate getFlightDate() { return flightDate; }
}