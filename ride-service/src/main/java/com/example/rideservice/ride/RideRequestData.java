package com.example.rideservice.ride;

public class RideRequestData {

    private Location from;
    private Location to;
    private String userId;

    public RideRequestData(Location from, Location to, String userId) {
        this.from = from;
        this.to = to;
        this.userId = userId;
    }

    private RideRequestData() {}

    public Location getTo() {
        return to;
    }

    public Location getFrom() {
        return from;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "RideRequestData{" +
                "from=" + from +
                ", to=" + to +
                ", userId='" + userId + '\'' +
                '}';
    }
}

record Location(Double latitude, Double longitude) {}
