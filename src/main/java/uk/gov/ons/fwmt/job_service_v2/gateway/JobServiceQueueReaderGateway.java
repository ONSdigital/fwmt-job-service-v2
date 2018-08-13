package uk.gov.ons.fwmt.job_service_v2.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface JobServiceQueueReaderGateway {
    @Gateway(requestChannel = "readQueue", replyChannel = "acknowledgeReadQueue")
    Object process(Object object);
}
