package com.example.rideservice;

public record BusinessEvent(String type, Object data) {

    private static final String VEHICLE_LOCATION_CHANGED = "vehicle-location-changed";
}
