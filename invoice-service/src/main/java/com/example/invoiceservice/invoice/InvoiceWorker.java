package com.example.invoiceservice.invoice;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InvoiceWorker {

    private static final Logger log = LoggerFactory.getLogger(InvoiceWorker.class);

    @ZeebeWorker(type = "SendInvoice", autoComplete = true, forceFetchAllVariables = true)
    public void invoiceService(final JobClient client, final ActivatedJob job) {
        log.info("Invoking invoiceService with variables: " + job.getVariablesAsMap());
    }
}
