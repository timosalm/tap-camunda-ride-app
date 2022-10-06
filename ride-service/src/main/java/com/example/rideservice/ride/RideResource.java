package com.example.rideservice.ride;

import com.example.rideservice.BusinessEvent;
import com.example.rideservice.RideServiceWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@RestController
@RequestMapping(RideResource.BASE_URI)
public class RideResource {

    static final String BASE_URI = "/api/v1/ride";
    private static final Logger log = LoggerFactory.getLogger(RideResource.class);

    private final RideApplicationService rideApplicationService;

    public RideResource(RideApplicationService rideApplicationService) {
        this.rideApplicationService = rideApplicationService;
    }

    @PostMapping
    public ResponseEntity<Void> sendRideRequestNotificationToDrivers(@NotNull @Valid @RequestBody RideRequestData rideRequestData) {
        log.info("sendRideRequestNotificationToDrivers called with data: " + rideRequestData);
        this.rideApplicationService.handleEvent(new BusinessEvent(UUID.randomUUID(), BusinessEvent.DRIVER_ACCEPTED, ""));
        return ResponseEntity.accepted().build();
    }
}
