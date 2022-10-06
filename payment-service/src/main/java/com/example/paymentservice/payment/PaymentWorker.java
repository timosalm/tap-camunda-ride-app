package com.example.paymentservice.payment;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PaymentWorker {

    private static final Logger log = LoggerFactory.getLogger(PaymentWorker.class);

    @ZeebeWorker(type = "ChargeCredit", autoComplete = true, forceFetchAllVariables = true)
    public void paymentService(final JobClient client, final ActivatedJob job) {
        log.info("Invoking paymentService with variables: " + job.getVariablesAsMap());
    }
}
