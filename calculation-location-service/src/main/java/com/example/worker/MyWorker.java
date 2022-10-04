package com.example.worker;

import com.example.ProcessVariables;
import com.example.service.CalculateLocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.camunda.zeebe.spring.client.annotation.ZeebeVariablesAsType;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;

@Component
public class MyWorker {

  private final static Logger LOG = LoggerFactory.getLogger(MyWorker.class);

  @Autowired
  private CalculateLocationService calculateLocationService;

  @ZeebeWorker(type = "calculate-location", autoComplete = true)
  public ProcessVariables invokeMyService(@ZeebeVariablesAsType ProcessVariables variables) {
    LOG.info("Invoking myService with variables: " + variables);

    //boolean result = calculateLocationService.myOperation(variables.getBusinessKey());
    //variables.setResult(result);
    return variables;
  //}

}
}
