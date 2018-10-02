package uk.gov.ons.fwmt.job_service_v2.config;

import org.aopalliance.aop.Advice;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryOperations;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.support.RetryTemplate;
import uk.gov.ons.fwmt.fwmtgatewaycommon.config.QueueNames;
import uk.gov.ons.fwmt.fwmtgatewaycommon.retry.CTPRetryPolicy;
import uk.gov.ons.fwmt.fwmtgatewaycommon.retry.CustomMessageRecover;
import uk.gov.ons.fwmt.job_service_v2.queuereceiver.JobServiceMessageReceiver;
import uk.gov.ons.fwmt.job_service_v2.retrysupport.DefaultListenerSupport;

import static uk.gov.ons.fwmt.fwmtgatewaycommon.config.QueueNames.ADAPTER_JOB_SVC_DLQ;
import static uk.gov.ons.fwmt.fwmtgatewaycommon.config.QueueNames.JOB_SVC_ADAPTER_DLQ;

@Configuration
public class QueueConfig {

  private int initialInterval;
  private int multiplier;
  private int maxInterval;

  public QueueConfig(@Value("${rabbitmq.initialinterval}") int initialInterval,
      @Value("#{new Double('${rabbitmq.multiplier}')}") int multiplier,
      @Value("$rabbitmq.maxInterval") int maxInterval) {
    this.initialInterval = initialInterval;
    this.multiplier = multiplier;
    this.maxInterval = maxInterval;
  }

  @Bean
  Queue adapterQueue() {
    return QueueBuilder.durable(QueueNames.JOBSVC_TO_ADAPTER_QUEUE)
        .withArgument("x-dead-letter-exchange", "")
        .withArgument("x-dead-letter-routing-key", JOB_SVC_ADAPTER_DLQ)
        .build();
  }

  @Bean
  Queue jobSvcQueue() {
    return QueueBuilder.durable(QueueNames.ADAPTER_TO_JOBSVC_QUEUE)
        .withArgument("x-dead-letter-exchange", "")
        .withArgument("x-dead-letter-routing-key", ADAPTER_JOB_SVC_DLQ)
        .build();
  }

  @Bean
  public Queue adapterDeadLetterQueue() {
    return QueueBuilder.durable(ADAPTER_JOB_SVC_DLQ).build();
  }

  @Bean
  public Queue jobSvsDeadLetterQueue() {
    return QueueBuilder.durable(JOB_SVC_ADAPTER_DLQ).build();
  }

  @Bean
  public DirectExchange adapterExchange() {
    return new DirectExchange(QueueNames.RM_JOB_SVC_EXCHANGE);
  }

  @Bean
  public Binding adapterBinding(@Qualifier("adapterQueue") Queue queue, DirectExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(QueueNames.JOB_SVC_RESPONSE_ROUTING_KEY);
  }

  @Bean
  public Binding jobSvcBinding(@Qualifier("jobSvcQueue") Queue queue, DirectExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(QueueNames.JOB_SVC_REQUEST_ROUTING_KEY);
  }

  @Bean
  public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
      MessageListenerAdapter listenerAdapter, RetryOperationsInterceptor interceptor) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();

    Advice[] adviceChain = {interceptor};

    container.setAdviceChain(adviceChain);
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(QueueNames.ADAPTER_TO_JOBSVC_QUEUE);
    container.setMessageListener(listenerAdapter);
    return container;
  }

  @Bean
  public MessageListenerAdapter listenerAdapter(JobServiceMessageReceiver receiver) {
    return new MessageListenerAdapter(receiver, "receiveMessage");
  }

  @Bean
  public RetryOperationsInterceptor interceptor(RetryOperations retryTemplate) {
    RetryOperationsInterceptor interceptor = new RetryOperationsInterceptor();
    interceptor.setRecoverer(new CustomMessageRecover());
    interceptor.setRetryOperations(retryTemplate);
    return interceptor;
  }

  @Bean
  public RetryTemplate retryTemplate() {
    RetryTemplate retryTemplate = new RetryTemplate();

    ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
    backOffPolicy.setInitialInterval(initialInterval);
    backOffPolicy.setMultiplier(multiplier);
    backOffPolicy.setMaxInterval(maxInterval);
    retryTemplate.setBackOffPolicy(backOffPolicy);

    CTPRetryPolicy ctpRetryPolicy = new CTPRetryPolicy();
    retryTemplate.setRetryPolicy(ctpRetryPolicy);

    retryTemplate.registerListener(new DefaultListenerSupport());

    return retryTemplate;
  }
}
