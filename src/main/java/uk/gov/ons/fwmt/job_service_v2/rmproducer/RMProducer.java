package uk.gov.ons.fwmt.job_service_v2.rmproducer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.ons.fwmt.fwmtgatewaycommon.DummyTMResponse;

import static uk.gov.ons.fwmt.job_service_v2.ApplicationConfig.RM_ADAPTER_QUEUE;

@Slf4j
@Component
public class RMProducer {

  @Autowired
  private RabbitTemplate template;


  public void send(DummyTMResponse dummyTMResponse) {
    this.template.convertAndSend(RM_ADAPTER_QUEUE, dummyTMResponse);
    log.info("Message sent to queue" + (dummyTMResponse.toString()) + "...");
  }
}
