package uk.gov.ons.fwmt.job_service_v2.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import uk.gov.ons.fwmt.fwmtgatewaycommon.config.QueueConfig;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.Address;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCancelJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.MockMessage;
import uk.gov.ons.fwmt.job_service_v2.IntegrationTestConfig;
import uk.gov.ons.fwmt.job_service_v2.helper.TestReceiver;

import javax.annotation.PostConstruct;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Component
@Slf4j
@ActiveProfiles("integration")
@Import({IntegrationTestConfig.class, TestReceiver.class})
public class RMIntergrationTest {

  @Value("${server.port}")
  private int port;
  @Value("${mock.port}")
  private int mockPort;

  private String mockUrl = "http://localhost:9099";

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  private void sendCreateMessage() {

    FWMTCreateJobRequest fwmtCreateJobRequest = new FWMTCreateJobRequest();
    Address address = new Address();
    address.setLatitude(BigDecimal.valueOf(61.7921776));
    address.setLongitude(BigDecimal.valueOf(34.3739957));
    address.setLine1("886");
    address.setLine2("Prairie Rose");
    address.setLine3("Trail");
    address.setLine4("RU");
    address.setPostCode("188961");
    address.setTownName("Borodinskiy");

    fwmtCreateJobRequest.setJobIdentity("1234");
    fwmtCreateJobRequest.setSurveyType("HH");
    fwmtCreateJobRequest.setDueDate(LocalDate.parse("20180216", DateTimeFormatter.BASIC_ISO_DATE));
    fwmtCreateJobRequest.setAddress(address);
    fwmtCreateJobRequest.setActionType("Create");

    String JSONJobRequest = null;
    try {
      JSONJobRequest = objectMapper.writeValueAsString(fwmtCreateJobRequest);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    log.info("Message send to queue"+ JSONJobRequest);
    rabbitTemplate.convertAndSend(QueueConfig.RM_JOB_SVC_EXCHANGE, QueueConfig.JOB_SVC_REQUEST_ROUTING_KEY, JSONJobRequest);

  }

  private void SendCancelMessage() {

    FWMTCancelJobRequest fwmtCancelJobRequest = new FWMTCancelJobRequest();
    fwmtCancelJobRequest.setActionType("Cancel");
    fwmtCancelJobRequest.setJobIdentity("1234");
    fwmtCancelJobRequest.setReason("wrong address");

    String JSONJobRequest = null;
    try {
      JSONJobRequest = objectMapper.writeValueAsString(fwmtCancelJobRequest);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    rabbitTemplate.convertAndSend(QueueConfig.RM_JOB_SVC_EXCHANGE, QueueConfig.JOB_SVC_REQUEST_ROUTING_KEY, JSONJobRequest);

  }

  @Test
  public void receiveRMCreateMessage() throws InterruptedException {

    RestTemplate restTemplate = new RestTemplate();

    restTemplate.getForObject(mockUrl + "/logger/reset", Void.class);

    sendCreateMessage();

    Thread.sleep(7000);

    MockMessage[] messages = restTemplate.getForObject(mockUrl + "/logger/allMessages", MockMessage[].class);

    assertEquals(1, messages.length);

  }

  @Test
  public void receiveRMCancelMessage() throws InterruptedException {

  RestTemplate restTemplate = new RestTemplate();

  restTemplate.getForObject(mockUrl + "/logger/reset", Void.class);

  SendCancelMessage();

  Thread.sleep(7000);

  MockMessage[] messages = restTemplate.getForObject(mockUrl + "/logger/allMessages", MockMessage[].class);

  assertEquals(1, messages.length);

  }

}
