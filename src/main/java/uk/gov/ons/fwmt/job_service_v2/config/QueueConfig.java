package uk.gov.ons.fwmt.job_service_v2.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import uk.gov.ons.fwmt.job_service_v2.queuereceiver.JobServiceMessageReceiver;
import uk.gov.ons.fwmt.job_service_v2.retrysupport.DefaultListenerSupport;

@Configuration
public class QueueConfig {

  public static final String DEAD_LETTER_QUEUE_NAME = "adapter-jobSvc.DLQ";

  @Bean
  Queue adapterQueue() {
    return new Queue(uk.gov.ons.fwmt.fwmtgatewaycommon.config.QueueConfig.JOBSVC_TO_ADAPTER_QUEUE, true);
  }

  //  @Bean
  //  Queue jobsvcQueue() {
  //    return new Queue(uk.gov.ons.fwmt.fwmtgatewaycommon.config.QueueConfig.ADAPTER_TO_JOBSVC_QUEUE, true);
  //  }

  @Bean
  Queue jobsvcQueue() {
    return QueueBuilder.durable(uk.gov.ons.fwmt.fwmtgatewaycommon.config.QueueConfig.ADAPTER_TO_JOBSVC_QUEUE)
        .withArgument("x-dead-letter-exchange", "")
        .withArgument("x-dead-letter-routing-key", DEAD_LETTER_QUEUE_NAME)
        .build();
  }

  @Bean
  Queue deadLetterQueue() {
    return QueueBuilder.durable(DEAD_LETTER_QUEUE_NAME).build();
  }

  @Bean
  TopicExchange adapterExchange() {
    return new TopicExchange(uk.gov.ons.fwmt.fwmtgatewaycommon.config.QueueConfig.RM_JOB_SVC_EXCHANGE);
  }

  @Bean
  Binding adapterBinding(@Qualifier("adapterQueue") Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(uk.gov.ons.fwmt.fwmtgatewaycommon.config.QueueConfig.JOB_SVC_RESPONSE_ROUTING_KEY);
  }

  @Bean
  Binding jobsvcBinding(@Qualifier("jobsvcQueue") Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(uk.gov.ons.fwmt.fwmtgatewaycommon.config.QueueConfig.JOB_SVC_REQUEST_ROUTING_KEY);
  }

  @Bean
  SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
      MessageListenerAdapter listenerAdapter) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(uk.gov.ons.fwmt.fwmtgatewaycommon.config.QueueConfig.ADAPTER_TO_JOBSVC_QUEUE);
    container.setMessageListener(listenerAdapter);
    container.setDefaultRequeueRejected(false);
    return container;
  }

  @Bean
  MessageListenerAdapter listenerAdapter(JobServiceMessageReceiver receiver) {
    return new MessageListenerAdapter(receiver, "receiveMessage");
  }

  @Bean
  public RetryTemplate retryTemplate() {
    RetryTemplate retryTemplate = new RetryTemplate();

    ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
    backOffPolicy.setInitialInterval(5000);
    backOffPolicy.setMultiplier(3.0);
    backOffPolicy.setMaxInterval(45000);
    retryTemplate.setBackOffPolicy(backOffPolicy);

    SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(3);
    retryPolicy.setMaxAttempts(3);
    retryTemplate.setRetryPolicy(retryPolicy);

    retryTemplate.registerListener(new DefaultListenerSupport());

    return retryTemplate;
  }
}
