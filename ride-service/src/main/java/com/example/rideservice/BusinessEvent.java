package com.example.rideservice;

import java.util.UUID;

public record BusinessEvent(UUID id, String type, Object data) {

    public static final String NOTIFY_DRIVER = "notify-driver";
    public static final String MATCH_CONFIRMED = "match-confirmed";
}
