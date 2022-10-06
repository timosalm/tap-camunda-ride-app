package com.example.zeebeeventbridge.eventbridge;

public class RideRequestData extends EventData {

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
}

record Location(Double latitude, Double longitude) {}
