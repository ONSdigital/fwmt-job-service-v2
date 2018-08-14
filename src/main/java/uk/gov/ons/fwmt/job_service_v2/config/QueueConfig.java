package uk.gov.ons.fwmt.job_service_v2.config;


import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class QueueConfig {

    @Value("${queue.rabbit.read.name}")
    public transient String readQueueName;
    @Value("${queue.rabbit.host}")
    private transient String rabbitMqHost;
    @Value("${queue.rabbit.port}")
    private transient int rabbitMqPort;

    @Bean
    public ConnectionFactory connectionFactory() {
        final CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(rabbitMqHost);
        connectionFactory.setPort(rabbitMqPort);
        return connectionFactory;
    }


}
