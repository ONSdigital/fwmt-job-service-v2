package uk.gov.ons.fwmt.job_service_v2.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/rabbitHealth")
public class RabbitCheckHealthController {

  @Autowired
  ConnectionFactory factory;

  @RequestMapping(value = "/queue", method = RequestMethod.GET, produces = "application/json")
  public boolean canAccessQueue(@RequestParam("qname") String qname) {
    RabbitAdmin rabbitAdmin = new RabbitAdmin(factory);
    String result1 = rabbitAdmin.getQueueProperties(qname).getProperty("QUEUE_NAME");
    if (qname.equals(result1)) {
      return true;
    }
    return false;
  }

}
