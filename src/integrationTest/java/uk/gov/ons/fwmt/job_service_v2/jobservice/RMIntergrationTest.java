package uk.gov.ons.fwmt.job_service_v2.jobservice;

import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendDeleteJobRequestMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.client.RestTemplate;


import javax.annotation.PostConstruct;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class RMIntergrationTest {

    @Value("${server.port}")
    private  int port;
    @Value("${mock.port}")
    private int mockPort;
    @Autowired
    private TaskExecutor taskExecutor;

    private String url;
    private String mockUrl;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    @Qualifier("adapterToJobSvcQueue")
    private Queue queue;

    @Autowired
    private Exchange exchange;

    @Autowired
    private ObjectMapper objectMapper;

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

        rabbitTemplate.convertAndSend(exchange.getName(), "job.svc.job.request.create", json);
        log.info("Message send to queue", json);
    }

    private void SendCancelMessage() {

        JSONObject json = new JSONObject();
        json.put("actionType","Cancel");
        json.put("jobIdentity","1234");
        json.put("reason","wrong address");

        rabbitTemplate.convertAndSend(exchange.getName(), "job.svc.job.request.cancel", json);
    }

    @PostConstruct
    public void postConstruct() {
        url = "http://localhost:" + Integer.toString(port);
        mockUrl = "http://localhost:" + Integer.toString(mockPort);
    }

    @Test
    public void receiveRMCreateMessage() {

        RestTemplate restTemplate = new RestTemplate();

        sendCreateMessage();

        restTemplate.getForObject(mockUrl + "/logger/allMessages", String[].class);

        String[] messages = restTemplate.getForObject(mockUrl + "/logger/allMessages", String[].class);

        assertEquals(1, messages.length);

        // first line, auth="1234", quota="100", id="tla_1-REISS1-001-100"
        // allocation
//        assertFalse(messages[0]);
//        assertEquals("MessageQueueWs", messages[0].endpoint);
//        assertEquals("sendCreateJobRequestMessage", messages[0].method);
//        assertTrue(messages[0].requestRawHtml.contains("tla_1-REISS1-001-100")); messages = restTemplate.getForObject(mockUrl + "/logger/allMessages", String[].class);

    }

    @Test
    public void receiveRMCancelMessage() {

        RestTemplate restTemplate = new RestTemplate();

        SendCancelMessage();

        restTemplate.getForObject(mockUrl + "/logger/allMessages", String[].class);

        String[] messages = restTemplate.getForObject(mockUrl + "/logger/allMessages", String[].class);

        assertEquals(3, messages.length);

    }


}
