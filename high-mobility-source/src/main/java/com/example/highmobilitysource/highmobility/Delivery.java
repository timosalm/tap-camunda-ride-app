package com.example.highmobilitysource.highmobility;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
record Delivery(Vehicle vehicle, Event event) {}

@JsonIgnoreProperties(ignoreUnknown = true)
record Vehicle(String vin) {}

@JsonIgnoreProperties(ignoreUnknown = true)
record Event(String type, Date received_at, String action) {}