package uk.gov.ons.fwmt.job_service_v2.config;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.ws.MarshallingWebServiceOutboundGateway;
import org.springframework.messaging.MessageHandler;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;


@Configuration
public class TotalMobileServiceConfig {

    @Value("${totalmobile.url}")
    private transient String tmOutboundUrl;
    @Value("${totalmobile.message-queue-package}")
    private transient String tmOutBoundPath;
    @Value("${totalmobile.username}")
    private String username;
    @Value("${totalmobile.password}")
    private String password;

    @Bean
    public MessageHandler wsOutBoundGateway() throws Exception {
        final MarshallingWebServiceOutboundGateway gw = new MarshallingWebServiceOutboundGateway(tmOutboundUrl,jaxb2Marshaller(),jaxb2Marshaller());
        gw.setMessageSender(clientTimeouts());
        return gw;
    }

    @Bean
    public HttpComponentsMessageSender clientTimeouts() throws Exception {
        final HttpComponentsMessageSender httpComponentsMessageSender = new HttpComponentsMessageSender();
        httpComponentsMessageSender.setConnectionTimeout(60000);
        httpComponentsMessageSender.setReadTimeout(60000);
        httpComponentsMessageSender.setCredentials(new UsernamePasswordCredentials(username, password));
        httpComponentsMessageSender.afterPropertiesSet();
        return httpComponentsMessageSender;
    }

    @Bean
    public Jaxb2Marshaller jaxb2Marshaller() {
        final Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPath(tmOutBoundPath);
        return jaxb2Marshaller;
    }

}
