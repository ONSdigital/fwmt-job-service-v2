/*
 * Copyright.. etc
 */

package uk.gov.ons.fwmt.job_service_v2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import uk.gov.ons.fwmt.job_service_v2.queuereceiver.RMJobCreate;

/**
 * Main entry point into the TM Gateway
 *
 * @author Chris Hardman
 */

@Slf4j
@SpringBootApplication
public class ApplicationConfig {

  private static final String RM_ADAPTER_QUEUE = "tmConicalQueue";
  private static final String TOPIC_EXCHANGE_NAME = "rm-jobsvc-adapterExchange";
  private static final String ADAPTER_QUEUE_NAME = "adapter-jobSvc";

  public static void main(String[] args) {
    SpringApplication.run(ApplicationConfig.class, args);
    log.debug("Started application");
  }

  @Bean
  Queue tmConicalQueue() {
    return new Queue(RM_ADAPTER_QUEUE, false);
  }

  @Bean
  Binding tmConicalQueueBinding(@Qualifier("tmConicalQueue") Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with("job.svc.job.response.#");
  }

  @Bean
  Queue adapterQueue() {
    return new Queue(ADAPTER_QUEUE_NAME, false);
  }

  @Bean
  TopicExchange adapterExchange() {
    return new TopicExchange(TOPIC_EXCHANGE_NAME);
  }

  @Bean
  Binding adapterBinding(@Qualifier("adapterQueue") Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with("job.svc.job.request.#");
  }

  @Bean
  SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
      MessageListenerAdapter listenerAdapter) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(ADAPTER_QUEUE_NAME);
    container.setMessageListener(listenerAdapter);
    return container;
  }

  @Bean
  MessageListenerAdapter listenerAdapter(RMJobCreate receiver) {
    return new MessageListenerAdapter(receiver, "receiveMessage");
  }
}
