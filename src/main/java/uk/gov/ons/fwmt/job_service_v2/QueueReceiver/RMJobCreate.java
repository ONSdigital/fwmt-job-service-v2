package uk.gov.ons.fwmt.job_service_v2.QueueReceiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.fwmt.job_service_v2.service.TMJobConverterService;

import java.util.concurrent.CountDownLatch;

@Component
public class RMJobCreate {
  private CountDownLatch latch = new CountDownLatch(1);

  @Autowired
  private TMJobConverterService jobService;

  public void receiveMessage(String message) {
    System.out.println("Received create job request <" + message + ">");
    jobService.convertMessageFromQueue(message);
    latch.countDown();
  }

  public CountDownLatch getLatch() {
    return latch;
  }
}
