package uk.gov.ons.fwmt.job_service_v2.rmproducer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.fwmt.fwmtgatewaycommon.config.QueueConfig;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.DummyTMResponse;

@Slf4j
@Component
public class RMProducer {

  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  private RabbitTemplate template;

  public void send(DummyTMResponse dummyTMResponse) {
    try {
      final String dummyResponseStr = objectMapper.writeValueAsString(dummyTMResponse);
      log.info("Message sent to queue :{}",dummyResponseStr);
      template.convertAndSend(QueueConfig.JOBSVC_TO_ADAPTER_QUEUE, dummyResponseStr);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }
}
