package com.example.highmobilitysource.highmobility;

import java.util.Date;

public record VehicleLocation(String vin, Double latitude, Double longitude, Double heading, Date timestamp) {
}
