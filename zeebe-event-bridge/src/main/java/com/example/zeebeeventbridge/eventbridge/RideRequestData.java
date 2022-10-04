package com.example.zeebeeventbridge.eventbridge;

public record RideRequestData(Location from, Location to) {}

record Location(Double latitude, Double longitude) {}
