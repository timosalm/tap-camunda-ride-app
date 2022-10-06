package com.example.rideservice;

import java.util.UUID;

public record BusinessEvent(UUID id, String type, Object data) {

    public static final String VEHICLE_LOCATION_CHANGED = "vehicle-location-changed";
    public static final String RIDE_REQUESTED = "ride-requested";
    public static final String NOTIFY_DRIVER = "notify-driver";
    public static final String MATCH_CONFIRMED = "match-confirmed";
    public static final String DRIVER_ACCEPTED = "driver-accepted";
    public static final String RIDER_PICKED_UP = "rider-picked-up";
    public static final String RIDE_FINISHED = "ride-finished";

}
