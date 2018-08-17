package uk.gov.ons.fwmt.job_service_v2.rmproducer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import uk.gov.ons.fwmt.fwmtgatewaycommon.DummyTMResponse;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.ons.fwmt.job_service_v2.ApplicationConfig.RM_ADAPTER_QUEUE;

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
    verify(template).convertAndSend(RM_ADAPTER_QUEUE, "dummyResponseStr");
  }
}