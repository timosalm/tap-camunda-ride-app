package com.example.highmobilitysource.highmobility;

import java.util.Date;

public record HighMobilityVehicleLocationData(Coordinates coordinates) {

    public Double getLatitude() {
        return coordinates.data().latitude().value();
    }

    public Double getLongitude() {
        return coordinates.data().longitude().value();
    }
}

record Coordinates(CoordinatesData data, Date timestamp) { }

record CoordinatesData(CoordinatesDataValue latitude, CoordinatesDataValue longitude) { }

record CoordinatesDataValue(Double value) { }