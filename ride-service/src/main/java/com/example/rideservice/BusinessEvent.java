package com.example.rideservice;

import java.util.UUID;

public record BusinessEvent(UUID id, String type, Object data) {

    public static final String VEHICLE_LOCATION_CHANGED = "vehicle-location-changed";
    public static final String RIDE_REQUESTED = "ride-requested";

}
