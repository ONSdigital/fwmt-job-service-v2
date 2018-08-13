package uk.gov.ons.fwmt.job_service_v2.flow;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.amqp.Amqp;

@Configuration
public class JobServiceFlow {

    @Bean
    public IntegrationFlow amqpInbound(ConnectionFactory connectionFactory) {
        return IntegrationFlows.from(Amqp.inboundAdapter(connectionFactory, "jobsvc-adapter"))
                .handle(m-> m.getPayload())
                .get();
    }

}
