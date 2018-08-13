package uk.gov.ons.fwmt.job_service_v2.flow;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.amqp.Amqp;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.messaging.MessageHandler;
import uk.gov.ons.fwmt.fwmtgatewaycommon.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.job_service_v2.config.TotalMobileServiceConfig;
import uk.gov.ons.fwmt.job_service_v2.transformer.FWMTCreateJobRequestTransformer;

@Configuration
public class JobServiceFlow {

    @Autowired
    TotalMobileServiceConfig totalMobileServiceConfig;
    @Autowired
    FWMTCreateJobRequestTransformer fWMTCreateJobRequestTransformer;

    @Bean
    public IntegrationFlow readJobServiceQueueAndCallTm(ConnectionFactory connectionFactory) throws Exception {
        return IntegrationFlows.from(Amqp.inboundAdapter(connectionFactory, "testqueue"))
                .transform(new JsonToObjectTransformer(FWMTCreateJobRequest.class))
                .wireTap(f -> f.handle(logger()))
                .transform(fWMTCreateJobRequestTransformer)
              //  .handle(totalMobileServiceConfig.wsOutBoundGateway())
                .get();
    }


    @Bean
    public MessageHandler logger() {
        LoggingHandler loggingHandler =  new LoggingHandler(LoggingHandler.Level.INFO.name());
        loggingHandler.setLoggerName("jobsvc");
        return loggingHandler;
    }
}
