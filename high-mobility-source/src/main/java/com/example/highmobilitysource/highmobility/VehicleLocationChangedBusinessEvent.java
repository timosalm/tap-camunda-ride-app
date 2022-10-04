package com.example.highmobilitysource.highmobility;

public class VehicleLocationChangedBusinessEvent {

    private static final String VEHICLE_LOCATION_CHANGED = "vehicle-location-changed";

    private String type;
    private VehicleLocation data;

    public VehicleLocationChangedBusinessEvent(final VehicleLocation location) {
        this.type = VEHICLE_LOCATION_CHANGED;
        this.data = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public VehicleLocation getData() {
        return data;
    }

    public void setData(VehicleLocation location) {
        this.data = location;
    }
}
