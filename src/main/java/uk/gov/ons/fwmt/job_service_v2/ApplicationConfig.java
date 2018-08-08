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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import uk.gov.ons.fwmt.job_service_v2.QueueReceiver.RMJobCreate;


/**
 * Main entry point into the Legacy Gateway
 *
 * @author Thomas Poot
 * @author James Berry
 * @author Jacob Harrison
 */

@Slf4j
@SpringBootApplication
@EnableAsync
public class ApplicationConfig {

  @Value("${service.resource.username}")
  private String userName;
  @Value("${service.resource.password}")
  private String password;


  static final String topicExchangeName = "rm-create-exchange";

  static final String queueName = "rm-create";


  @Bean
  Queue queue() {
    return new Queue(queueName, false);
  }

  @Bean
  TopicExchange exchnage() {
    return new TopicExchange(topicExchangeName);
  }

  @Bean
  Binding binding(Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with("foo.bar.#");
  }

  @Bean
  SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                           MessageListenerAdapter listenerAdapter) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(queueName);
    container.setMessageListener(listenerAdapter);
    return container;
  }

  @Bean
  MessageListenerAdapter listenerAdapter(RMJobCreate receiver) {
    return new MessageListenerAdapter(receiver, "receiveMessage");
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    SpringApplication.run(ApplicationConfig.class, args);
    log.debug("Started application");
  }

  /**
   * @param
   * @return
   */
  @Bean
  CommandLineRunner init() {
    return (args) -> {
      //storageService.deleteAll();
      //storageService.init();
    };
  }

//  @Bean(name="processExecutor")
//  public TaskExecutor workExecutor() {
//    ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
//    threadPoolTaskExecutor.setThreadNamePrefix("Async-");
//    threadPoolTaskExecutor.setCorePoolSize(3);
//    threadPoolTaskExecutor.setMaxPoolSize(3);
//    threadPoolTaskExecutor.setQueueCapacity(600);
//    threadPoolTaskExecutor.setTaskDecorator(new MDCTaskDecorator());
//    threadPoolTaskExecutor.afterPropertiesSet();
//    return threadPoolTaskExecutor;
//  }
//
//  @Bean
//  public RestTemplate resourcesRestTemplate(RestTemplateBuilder builder) {
//    return builder
//        .basicAuthorization(userName, password)
//        .interceptors(Collections.singletonList(new CorrelationIdInterceptor()))
//        .build();
//  }

}
