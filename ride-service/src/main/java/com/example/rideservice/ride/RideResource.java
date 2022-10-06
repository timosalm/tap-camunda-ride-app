package com.example.rideservice.ride;

import com.example.rideservice.BusinessEvent;
import com.example.rideservice.RideServiceWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Timer;
import java.util.TimerTask;
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

    @PostMapping("/ride-request-notification")
    public ResponseEntity<Void> sendRideRequestNotificationToDrivers(
            @NotNull @Valid @RequestBody RideRequestNotificationData rideRequestNotificationData
    ) {
        log.info("sendRideRequestNotificationToDrivers called with data: " + rideRequestNotificationData);

        this.rideApplicationService.sendRideRequestNotificationToDrivers(rideRequestNotificationData);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/match-confirmation-notification")
    public ResponseEntity<Void> sendMatchConfirmationNotification(
            @NotNull @Valid @RequestBody MatchConfirmationNotificationData matchConfirmationNotificationData
    ) {
        log.info("sendMatchConfirmationNotification called with data: " + matchConfirmationNotificationData);

        this.rideApplicationService.sendMatchConfirmationNotification(matchConfirmationNotificationData);

        return ResponseEntity.accepted().build();
    }



}
