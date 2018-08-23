package uk.gov.ons.fwmt.job_service_v2.rmproducer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import uk.gov.ons.fwmt.fwmtgatewaycommon.config.QueueConfig;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.DummyTMResponse;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RMProducerTest {

  @InjectMocks
  RMProducer rmProducer;

  @Mock
  RabbitTemplate template;

  @Mock
  ObjectMapper objectMapper;

  @Test
  public void send() throws JsonProcessingException {
    //Given
    DummyTMResponse dummyTMResponse = new DummyTMResponse();
    dummyTMResponse.setIdentity("test");
    when(objectMapper.writeValueAsString(eq(dummyTMResponse))).thenReturn("dummyResponseStr");

    //When
    rmProducer.send(dummyTMResponse);

    //Then
    verify(objectMapper).writeValueAsString(eq(dummyTMResponse));
    verify(template).convertAndSend(QueueConfig.JOBSVC_TO_ADAPTER_QUEUE,"dummyResponseStr");

  }
}