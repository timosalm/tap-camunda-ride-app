package com.example.zeebeeventbridge.eventbridge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.ParameterizedType;
import java.util.UUID;

public record BusinessEvent(UUID id, String type, String data) {

    public static final String RIDE_REQUESTED = "ride-requested";

    public <T> T getDataAsObject(Class<T> valueType) throws JsonProcessingException {
        var objectMapper = new ObjectMapper();
        return objectMapper.readValue(data, valueType);
    }
}
