package com.example.rideservice.ride;

import java.util.UUID;

public record MatchConfirmationNotificationData(UUID userId, String driver) { }
