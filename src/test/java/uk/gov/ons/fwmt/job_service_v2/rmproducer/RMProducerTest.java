package uk.gov.ons.fwmt.job_service_v2.rmproducer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import uk.gov.ons.fwmt.fwmtgatewaycommon.DummyTMResponse;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.ons.fwmt.job_service_v2.ApplicationConfig.RM_ADAPTER_QUEUE;

@RunWith(MockitoJUnitRunner.class)
public class RMProducerTest {

  @InjectMocks RMProducer rmProducer;
  @Mock Queue queue;
  @Mock RabbitTemplate template;
  @Captor ArgumentCaptor argCaptor;
  @Mock ObjectMapper objectMapper;


  @Test
  public void send() throws JsonProcessingException {
    //Given
    DummyTMResponse dummyTMResponse = new DummyTMResponse();
    dummyTMResponse.setIdentity("test");
    doNothing().when(template).convertAndSend(eq(RM_ADAPTER_QUEUE), eq(dummyTMResponse));
    when(objectMapper.writeValueAsString(eq(dummyTMResponse))).thenReturn("dummyResponseStr");

    //When
    rmProducer.send(dummyTMResponse);

    //Then
    verify(objectMapper).writeValueAsString(eq(dummyTMResponse));
    verify(template).convertAndSend(RM_ADAPTER_QUEUE,"dummyResponseStr");

  }
}