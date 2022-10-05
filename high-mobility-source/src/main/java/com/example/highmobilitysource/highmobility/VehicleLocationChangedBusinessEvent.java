package com.example.highmobilitysource.highmobility;

import java.util.UUID;

public class VehicleLocationChangedBusinessEvent {

    private static final String VEHICLE_LOCATION_CHANGED = "vehicle-location-changed";

    private final UUID id;
    private final String type;
    private final VehicleLocation data;

    public VehicleLocationChangedBusinessEvent(final VehicleLocation location) {
        this.id = UUID.randomUUID();
        this.type = VEHICLE_LOCATION_CHANGED;
        this.data = location;
    }

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
