package com.example.zeebeeventbridge.eventbridge;

public class RideRequestData extends EventData {

    private Location from;
    private Location to;

    public RideRequestData(Location from, Location to) {
        this.from = from;
        this.to = to;
    }

    private RideRequestData() {}

    public Location getTo() {
        return to;
    }

    public Location getFrom() {
        return from;
    }
}

record Location(Double latitude, Double longitude) {}
