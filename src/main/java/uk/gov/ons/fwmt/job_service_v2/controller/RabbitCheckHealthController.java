package uk.gov.ons.fwmt.job_service_v2.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
public class RabbitCheckHealthController {

  @Autowired
  ConnectionFactory factory;

  @RequestMapping(value = "/rabbitHealth", method = RequestMethod.GET, produces = "application/json")
  public List<String> rabbitHealth(){

    RabbitAdmin rabbitAdmin = new RabbitAdmin(factory);

    List<String> results = new ArrayList<>();

    String result1 = rabbitAdmin.getQueueProperties("jobsvc-adapter").getProperty("QUEUE_NAME");
    String result2 = rabbitAdmin.getQueueProperties("adapter-jobSvc").getProperty("QUEUE_NAME");
    String result3 = rabbitAdmin.getQueueProperties("adapter-rm").getProperty("QUEUE_NAME");

    results.add(result1);
    results.add(result2);
    results.add(result3);

    return results;
  }

}
