package com.example.zeebeeventbridge.eventbridge;

import java.util.UUID;

public class RideProgressData extends EventData {

    private UUID userId;

    public RideProgressData(UUID userId) {
        this.userId = userId;
    }

    private RideProgressData() {

    }

    public UUID getUserId() {
        return userId;
    }
}
