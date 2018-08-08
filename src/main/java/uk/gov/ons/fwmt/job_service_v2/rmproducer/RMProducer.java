package uk.gov.ons.fwmt.job_service_v2.rmproducer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.ons.fwmt.job_service_v2.dto.UnknownDto;

@Slf4j
@Component
public class RMProducer {

  private final Queue queue;

  private final RabbitTemplate template;

  @Autowired public RMProducer(@Qualifier("tmConicalQueue") Queue queue, RabbitTemplate template) {
    this.queue = queue;
    this.template = template;
  }

  public void send(UnknownDto unknownDto) {
    this.template.convertAndSend(queue.getName(), unknownDto);
    log.info("Sent" + unknownDto.toString() + "...");
  }
}
