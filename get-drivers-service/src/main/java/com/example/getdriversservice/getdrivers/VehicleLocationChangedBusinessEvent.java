package com.example.getdriversservice.getdrivers;

import java.util.Date;
import java.util.UUID;

public class VehicleLocationChangedBusinessEvent {

    public static final String TYPE = "vehicle-location-changed";

    private UUID id;
    private String type;
    private VehicleLocation data;

    public VehicleLocationChangedBusinessEvent(final VehicleLocation location) {
        this.id = UUID.randomUUID();
        this.type = TYPE;
        this.data = location;
    }

    private VehicleLocationChangedBusinessEvent() {}

    public String getType() {
        return type;
    }

    public VehicleLocation getData() {
        return data;
    }

    public UUID getId() {
        return id;
    }
}

 record VehicleLocation(String vin, Double latitude, Double longitude, Double heading, Date timestamp) {
}