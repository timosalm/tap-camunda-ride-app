package com.example.rideservice.ride;

import com.example.rideservice.BusinessEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RideApplicationService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${ride.zeebe-exchange-name}")
    private String zeebeExchangeName;

    public RideApplicationService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void handleEvent(BusinessEvent event) {
        if (BusinessEvent.RIDE_REQUESTED.equals(event.type())) {
            if (zeebeExchangeName == null || zeebeExchangeName.isEmpty()) {
                throw new RuntimeException("ride.zeebe-exchange-name not set");
            }
            rabbitTemplate.convertAndSend(this.zeebeExchangeName, "#", event);
        }
    }
}
