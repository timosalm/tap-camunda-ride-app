package com.example.highmobilitysource.highmobility;

public class VehicleLocationChangedBusinessEvent {

    private static final String VEHICLE_LOCATION_CHANGED = "vehicle-location-changed";

    private String type;
    private VehicleLocation location;

    public VehicleLocationChangedBusinessEvent(final VehicleLocation location) {
        this.type = VEHICLE_LOCATION_CHANGED;
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public VehicleLocation getLocation() {
        return location;
    }

    public void setLocation(VehicleLocation location) {
        this.location = location;
    }
}
