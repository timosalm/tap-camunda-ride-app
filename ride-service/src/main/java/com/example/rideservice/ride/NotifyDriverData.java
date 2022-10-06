package com.example.rideservice.ride;

import java.util.List;
import java.util.UUID;

public record NotifyDriverData(Location startingPoint, Location destination, UUID userId,
                                         String driver) { }