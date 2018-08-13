/*
 * Copyright.. etc
 */

package uk.gov.ons.fwmt.job_service_v2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;


@Slf4j
@SpringBootApplication
@EnableIntegration
@IntegrationComponentScan(basePackages = "uk.gov.ons.fwmt.job_service_v2")
public class ApplicationConfig {

  public static final String RM_ADAPTER_QUEUE = "tmConicalQueue";
  private static final String TOPIC_EXCHANGE_NAME = "rm-jobsvc-adapterExchange";
  private static final String ADAPTER_QUEUE_NAME = "adapter-jobSvc";

  public static void main(String[] args) {
    SpringApplication.run(ApplicationConfig.class, args);
    log.debug("Started application");
  }

//  @Bean
//  Queue adapterQueue() {
//    return new Queue(ADAPTER_QUEUE_NAME, false);
//  }
//
//  @Bean
//  TopicExchange adapterExchange() {
//    return new TopicExchange(TOPIC_EXCHANGE_NAME);
//  }
//
//  @Bean
//  Binding adapterBinding(@Qualifier("adapterQueue") Queue queue, TopicExchange exchange) {
//    return BindingBuilder.bind(queue).to(exchange).with("job.svc.job.request.#");
//  }
//
//  @Bean
//  SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
//      MessageListenerAdapter listenerAdapter) {
//    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//    container.setConnectionFactory(connectionFactory);
//    container.setQueueNames(ADAPTER_QUEUE_NAME);
//    container.setMessageListener(listenerAdapter);
//    return container;
//  }
//
//  @Bean
//  MessageListenerAdapter listenerAdapter(MessageParser receiver) {
//    return new MessageListenerAdapter(receiver, "receiveMessage");
//  }
}
