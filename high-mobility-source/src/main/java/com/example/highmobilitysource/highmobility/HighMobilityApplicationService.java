package com.example.highmobilitysource.highmobility;

import com.example.highmobilitysource.JwtConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HighMobilityApplicationService {

    private static final Logger log = LoggerFactory.getLogger(HighMobilityApplicationService.class);

    private final JwtConfigurationProperties jwtConfigurationProperties;
    private final RestTemplate restTemplate;
    private final RabbitStreamTemplate streamTemplate;

    public HighMobilityApplicationService(JwtConfigurationProperties jwtConfigurationProperties,
                                          RestTemplate restTemplate, RabbitStreamTemplate streamTemplate) {
        this.jwtConfigurationProperties = jwtConfigurationProperties;
        this.restTemplate = restTemplate;
        this.streamTemplate = streamTemplate;
    }

    public void handleVehicleLocationChangedEvent(String vin) {
        var vehicleLocation = fetchVehicleLocation(vin);
        log.info("Location data for vehicle " + vin + ": " + vehicleLocation.latitude() + ", " +
                vehicleLocation.longitude() + ", " + vehicleLocation.heading());
        streamTemplate.convertAndSend(new VehicleLocationChangedBusinessEvent(vehicleLocation));
    }

    private VehicleLocation fetchVehicleLocation(String vin) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtConfigurationProperties.getToken(vin));
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        var locationData = restTemplate.exchange(jwtConfigurationProperties.aud() + "/vehicle_location",
                HttpMethod.GET, httpEntity, HighMobilityVehicleLocationData.class).getBody();
        return locationData.toDomain(vin);
    }
}
