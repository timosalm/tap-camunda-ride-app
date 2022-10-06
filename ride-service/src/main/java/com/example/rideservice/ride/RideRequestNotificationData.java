package com.example.rideservice.ride;

import java.util.List;
import java.util.UUID;

public record RideRequestNotificationData(Location startingPoint, Location destination, UUID userId,
                                          List<String> drivers) { }

record Location(Double latitude, Double longitude) {}