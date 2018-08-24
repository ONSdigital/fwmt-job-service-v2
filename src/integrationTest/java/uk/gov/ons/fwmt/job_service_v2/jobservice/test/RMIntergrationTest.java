package uk.gov.ons.fwmt.job_service_v2.jobservice.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.ons.fwmt.fwmtgatewaycommon.config.QueueConfig;
import uk.gov.ons.fwmt.job_service_v2.jobservice.helper.TestReceiver;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import uk.gov.ons.fwmt.job_service_v2.jobservice.IntegrationTestConfig;

import javax.annotation.PostConstruct;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Component
@Slf4j
@ActiveProfiles("integration")
@Import({IntegrationTestConfig.class, TestReceiver.class})
public class RMIntergrationTest {

    @Value("${server.port}")
    private  int port;
    @Value("${mock.port}")
    private int mockPort;

//    @Autowired
//    private TaskExecutor taskExecutor;
//
    private String url;
    private String mockUrl;

    @Autowired
    private RabbitTemplate rabbitTemplate;


//    @Autowired
//    private ObjectMapper objectMapper;

    private void sendCreateMessage() {

        JSONObject json = new JSONObject();
        JSONObject address = new JSONObject();
        json.put("actionType","Create");
        json.put("jobIdentity","1234");
        json.put("surveyType","HH");
        json.put("preallocatedJob","true");
        json.put("mandatoryResourceAuthNo","1234");
        json.put("dueDate","20180216");
        address.put("line1","886");
        address.put("line2","Prairie Rose");
        address.put("line3","Trail");
        address.put("line4","RU");
        address.put("townName","Borodinskiy");
        address.put("postCode","188961");
        address.put("latitude","61.7921776");
        address.put("longitude","34.3739957");
        json.put("address", address);

        rabbitTemplate.convertAndSend(QueueConfig.RM_JOB_SVC_EXCHANGE, QueueConfig.JOB_SVC_REQUEST_ROUTING_KEY, json);
        log.info("Message send to queue", json);
    }
//
//    private void SendCancelMessage() {
//
//        JSONObject json = new JSONObject();
//        json.put("actionType","Cancel");
//        json.put("jobIdentity","1234");
//        json.put("reason","wrong address");
//
//        rabbitTemplate.convertAndSend(exchange.getName(), "job.svc.job.request.cancel", json);
//    }

    @PostConstruct
    public void postConstruct() {
        url = "http://localhost:" + Integer.toString(port) + "/tm/Mock";
        mockUrl = "http://localhost:" + Integer.toString(mockPort);
    }

    @Test
    public void receiveRMCreateMessage() {
        RestTemplate restTemplate = new RestTemplate();

        //sendCreateMessage();

        restTemplate.getForObject(mockUrl + "/logger/allMessages", String[].class);
//
//        String[] messages = restTemplate.getForObject(mockUrl + "/logger/allMessages", String[].class);
//
//        assertEquals(1, messages.length);

    }

    @Test
    public void receiveRMCancelMessage() {
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        SendCancelMessage();
//
//        restTemplate.getForObject(mockUrl + "/logger/allMessages", String[].class);
//
//        String[] messages = restTemplate.getForObject(mockUrl + "/logger/allMessages", String[].class);
//
//        assertEquals(3, messages.length);

    }


}
