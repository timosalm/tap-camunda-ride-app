package com.example.getdriversservice.getdrivers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeVariable;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import org.apache.lucene.util.SloppyMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class GetDriversWorker {

    private static final Logger log = LoggerFactory.getLogger(GetDriversWorker.class);

    private GetDriversRepository repository;

    public GetDriversWorker(GetDriversRepository repository) {
        this.repository = repository;
    }

    @ZeebeWorker(type = "CloseDrivers", autoComplete = true, forceFetchAllVariables = true)
    public Map<String, List<String>> getDriversService(final JobClient client, final ActivatedJob job, @ZeebeVariable Map<String, Double> startingPoint) {
        log.info("Invoking getDriversService with variables: " + job.getVariablesAsMap() + " " + startingPoint);

        var availableDrivers = this.repository.findAll().stream()
                .filter(d -> isClose(new Location(startingPoint.get("latitude"), startingPoint.get("longitude")),
                        new Location(d.getLatitude(), d.getLongitude())))
                .map(Driver::getVin)
                .toList();

        return Map.of("availableDrivers", availableDrivers);
    }

    private boolean isClose(Location startingPoint, Location driverLocation) {
        double distanceInMeters = SloppyMath.haversinMeters(driverLocation.latitude(), driverLocation.longitude(),
                startingPoint.latitude(), startingPoint.longitude());
        return distanceInMeters < 10000;
    }

    @RabbitListener(queues = "${spring.rabbitmq.stream.name}")
    public void listen(String in) throws JsonProcessingException {
        if (in.contains(VehicleLocationChangedBusinessEvent.TYPE)) {
            var objectMapper = new ObjectMapper();
            var vehicleLocation = objectMapper.readValue(in, VehicleLocationChangedBusinessEvent.class).getData();
            Optional<Driver> existingDriver = this.repository.findById(vehicleLocation.vin());
            if (existingDriver.isPresent() && existingDriver.get().getLastUpdate().after(vehicleLocation.timestamp())) {
                return;
            }
            var driver = new Driver(vehicleLocation.vin(), vehicleLocation.latitude(), vehicleLocation.longitude(),
                    vehicleLocation.timestamp());
            log.info("Updating driver information: " + driver);
            this.repository.save(driver);
        }
    }
}
