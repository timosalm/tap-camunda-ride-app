package com.example.highmobilitysource.highmobility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;

@RestController
@RequestMapping(HighMobilityResource.BASE_URI)
public class HighMobilityResource {

    private static final Logger log = LoggerFactory.getLogger(HighMobilityResource.class);

    static final String BASE_URI = "/api/v1/highmobility/events";

    private final HighMobilityApplicationService highMobilityApplicationService;

    @Value("${highmoblity.api-secret}")
    private String highMoblityApiSecret;

    HighMobilityResource(HighMobilityApplicationService highMobilityApplicationService) {
        this.highMobilityApplicationService = highMobilityApplicationService;
    }

    @PostMapping
    public ResponseEntity<Void> handleEvent(@RequestHeader("X-HM-Signature") String signature,
                                            @RequestHeader("X-HM-Event") String event, @RequestBody String body) {
        log.info("New event \"" + event + "\"");
        if (isInvalidSignature(signature, body)) {
            log.debug("Invalid signature " + signature + " for body: " + body);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Delivery delivery;
        try {
            delivery = new ObjectMapper().readValue(body, Delivery.class);
            log.debug("Delivery: " + delivery.toString());
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }

        if (event.equals("vehicle_location_changed")) {
            highMobilityApplicationService.handleVehicleLocationChangedEvent(delivery.vehicle().vin());
        }

        return ResponseEntity.accepted().build();
    }

    private boolean isInvalidSignature(String signature, String body) {
        var computed = String.format("sha1=%s", new HmacUtils(HmacAlgorithms.HMAC_SHA_1, highMoblityApiSecret).hmacHex(body));
        return !MessageDigest.isEqual(signature.getBytes(), computed.getBytes());
    }
}
