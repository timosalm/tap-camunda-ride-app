package com.example.zeebeeventbridge.eventbridge;

import java.util.UUID;

public class RideAcceptance extends EventData {

    private String driver;
    private UUID userId;

    public RideAcceptance(String driver, UUID userId) {
        this.driver = driver;
        this.userId = userId;
    }

    private RideAcceptance() {}

    public UUID getUserId() {
        return userId;
    }

    public String getDriver() {
        return driver;
    }
}
