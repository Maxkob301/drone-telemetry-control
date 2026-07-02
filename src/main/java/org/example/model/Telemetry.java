package org.example.model;

public class Telemetry {
    private double latitude;
    private double longitude;
    private double altitude;
    private double relativeAltitude;
    private double groundSpeed;
    private double heading;
    private long timestamp;

    public Telemetry(double latitude, double longitude, double altitude, double relativeAltitude, double groundSpeed, double heading, long timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.relativeAltitude = relativeAltitude;
        this.groundSpeed = groundSpeed;
        this.heading = heading;
        this.timestamp = timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    public void setGroundSpeed(double groundSpeed) {
        this.groundSpeed = groundSpeed;
    }

    public void setRelativeAltitude(double relativeAltitude) {
        this.relativeAltitude = relativeAltitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getRelativeAltitude() {
        return relativeAltitude;
    }

    public double getGroundSpeed() {
        return groundSpeed;
    }

    public double getHeading() {
        return heading;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Telemetry{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", altitude=" + altitude +
                ", relativeAltitude=" + relativeAltitude +
                ", groundSpeed=" + groundSpeed +
                ", heading=" + heading +
                ", timestamp=" + timestamp +
                '}';
    }
}
