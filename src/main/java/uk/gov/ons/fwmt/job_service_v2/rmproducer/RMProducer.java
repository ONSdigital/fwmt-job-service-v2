package uk.gov.ons.fwmt.job_service_v2.rmproducer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uk.gov.ons.fwmt.job_service_v2.dto.UnknownDto;

@Slf4j
@Component
public class RMProducer {

  @Autowired
  @Qualifier("tmConicalQueue")
  private Queue queue;

  @Autowired
  private RabbitTemplate template;

  @Scheduled(fixedDelay = 1000, initialDelay = 500)
  public void send(UnknownDto unknownDto) {
    this.template.convertAndSend(queue.getName(), unknownDto);
    log.info("Sent" + unknownDto.toString() + "...");
  }
}
