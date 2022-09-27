package com.example.highmobilitysource.highmobility;

import java.util.Date;

public record HighMobilityVehicleLocationData(Coordinates coordinates) {

    public VehicleLocation toDomain(String vin) {
        return new VehicleLocation(vin, coordinates.data().latitude().value(), coordinates.data().longitude().value(),
                coordinates.timestamp());
    }
}

record Coordinates(CoordinatesData data, Date timestamp) { }

record CoordinatesData(CoordinatesDataValue latitude, CoordinatesDataValue longitude) { }

record CoordinatesDataValue(Double value) { }