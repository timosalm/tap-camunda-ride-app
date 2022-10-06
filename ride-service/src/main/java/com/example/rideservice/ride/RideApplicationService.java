package com.example.rideservice.ride;

import com.example.rideservice.BusinessEvent;
import com.example.rideservice.RideServiceWebSocketHandler;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

@Service
public class RideApplicationService {

    private final RabbitTemplate rabbitTemplate;
    private final RideServiceWebSocketHandler rideServiceWebSocketHandler;

    @Value("${ride.zeebe-exchange-name}")
    private String zeebeExchangeName;

    public RideApplicationService(RabbitTemplate rabbitTemplate, RideServiceWebSocketHandler rideServiceWebSocketHandler) {
        this.rabbitTemplate = rabbitTemplate;
        this.rideServiceWebSocketHandler = rideServiceWebSocketHandler;
    }

    public void handleEvent(BusinessEvent event) {
        if (zeebeExchangeName == null || zeebeExchangeName.isEmpty()) {
            throw new RuntimeException("ride.zeebe-exchange-name not set");
        }
        rabbitTemplate.convertAndSend(this.zeebeExchangeName, "#", event);
    }

    public void sendRideRequestNotificationToDrivers(RideRequestNotificationData rideRequestNotificationData) {
        rideRequestNotificationData.drivers().forEach(driver -> {
                var notifyDriverData = new NotifyDriverData(rideRequestNotificationData.startingPoint(),
                        rideRequestNotificationData.destination(), rideRequestNotificationData.userId(), driver);
                this.rideServiceWebSocketHandler.publishEvent(
                        new BusinessEvent(UUID.randomUUID(), BusinessEvent.NOTIFY_DRIVER, notifyDriverData));
        });
          //      this.handleEvent(new BusinessEvent(UUID.randomUUID(),
          //      BusinessEvent.DRIVER_ACCEPTED, new RideAcceptance(driver, rideRequestNotificationData.userId()))));
    }

    public void sendMatchConfirmationNotification(MatchConfirmationNotificationData matchConfirmationNotificationData) {
        this.rideServiceWebSocketHandler.publishEvent(
                new BusinessEvent(UUID.randomUUID(), BusinessEvent.MATCH_CONFIRMED, matchConfirmationNotificationData));

        /*
        this.handleEvent(new BusinessEvent(UUID.randomUUID(), BusinessEvent.RIDER_PICKED_UP,
                new RideProgressData(matchConfirmationNotificationData.userId())));

        var _this = this;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                _this.handleEvent(new BusinessEvent(UUID.randomUUID(), BusinessEvent.RIDE_FINISHED,
                        new RideProgressData(matchConfirmationNotificationData.userId())));
            }
        }, 5000);
        */
    }
}
