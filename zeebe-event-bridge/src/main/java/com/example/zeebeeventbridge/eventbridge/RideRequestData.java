package com.example.zeebeeventbridge.eventbridge;

import java.util.UUID;

public class RideRequestData extends EventData {

    private Location from;
    private Location to;
    private UUID userId;

    public RideRequestData(Location from, Location to, UUID userId) {
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

    public UUID getUserId() {
        return userId;
    }
}

record Location(Double latitude, Double longitude) {}
