package com.example.highmobilitysource.highmobility;

import java.util.Date;

public record HighMobilityVehicleLocationData(Coordinates coordinates, Heading heading) {

    public VehicleLocation toDomain(String vin) {
        return new VehicleLocation(vin, coordinates().data().latitude().value(), coordinates().data().longitude().value(),
                heading().data().value(), coordinates().timestamp());
    }
}

record Coordinates(CoordinatesData data, Date timestamp) { }

record CoordinatesData(CoordinatesDataValue latitude, CoordinatesDataValue longitude) { }

record Heading(HeadingData data, Date timestamp) { }

record HeadingData(Double value, String unit) { }

record CoordinatesDataValue(Double value) { }
