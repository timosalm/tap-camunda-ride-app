package com.example.zeebeeventbridge.eventbridge;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

public class BusinessEvent {

    public static final String RIDE_REQUESTED = "ride-requested";
    public static final String DRIVER_ACCEPTED = "driver-accepted";
    public static final String RIDER_PICKED_UP = "rider-picked-up";
    public static final String RIDE_FINISHED = "ride-finished";

    private UUID id;
    private String type;
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXTERNAL_PROPERTY)
    @JsonSubTypes(value = {
            @JsonSubTypes.Type(value = RideRequestData.class, name = RIDE_REQUESTED),
            @JsonSubTypes.Type(value = RideAcceptance.class, name = DRIVER_ACCEPTED),
            @JsonSubTypes.Type(value = RideProgressData.class, name = RIDER_PICKED_UP),
    })
    private EventData data;

    private BusinessEvent() {
    }

    public BusinessEvent(UUID id, String type, EventData data) {
        this.id = id;
        this.type = type;
        this.data = data;
    }

    public UUID getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public EventData getData() {
        return data;
    }
}
