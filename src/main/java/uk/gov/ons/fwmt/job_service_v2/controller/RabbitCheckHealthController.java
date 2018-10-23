package uk.gov.ons.fwmt.job_service_v2.controller;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

public class RabbitCheckHealthController {

  private static final String ACTION_FIELD_QUEUE = "Action.Field";
  private static final String ACTION_FIELD_BINDING = "Action.Field.binding";

  @Value("${rabbitmq.username}") String username;
  @Value("${rabbitmq.password}") String password;
  @Value("${rabbitmq.hostname}") String hostname;
  @Value("${rabbitmq.port}") Integer port;
  @Value("${rabbitmq.virtualHost}") String virtualHost;

  private CachingConnectionFactory getRMConnectionFactory() {
    CachingConnectionFactory factory = new CachingConnectionFactory();
    factory.setHost(hostname);
    factory.setUsername(username);
    factory.setPassword(password);
    factory.setPort(port);
    factory.setVirtualHost(virtualHost);
    return factory;
  }


  @RequestMapping(value = "/rabbitHealth", method = RequestMethod.GET, produces = "application/json")
  public List<String> rabbitHealth(){

    RabbitAdmin rabbitAdmin = new RabbitAdmin(getRMConnectionFactory());

    List<String> results = new ArrayList<>();

    String result1 = rabbitAdmin.getQueueProperties("rm-adapter").getProperty("QUEUE_NAME");
    String result2 = rabbitAdmin.getQueueProperties("jobsvc-adapter").getProperty("QUEUE_NAME");
    String result3 = rabbitAdmin.getQueueProperties("adapter-jobSvc").getProperty("QUEUE_NAME");
    String result4 = rabbitAdmin.getQueueProperties("adapter-rm").getProperty("QUEUE_NAME");

    results.add(result1);
    results.add(result2);
    results.add(result3);
    results.add(result4);

    return results;
  }

}
