package uk.gov.ons.fwmt.job_service_v2.rmproducer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.fwmt.fwmtgatewaycommon.DummyTMResponse;

@Slf4j
@Component
public class RMProducer {

  private final Queue queue;

  private final RabbitTemplate template;

  @Autowired public RMProducer(RabbitTemplate template, Queue queue) {
    this.queue = queue;
    this.template = template;
  }

  public void send(DummyTMResponse dummyTMResponse) {
    this.template.convertAndSend(queue.getName(), dummyTMResponse);
    log.info("Message sent to queue" + (dummyTMResponse.toString()) + "...");
  }
}
