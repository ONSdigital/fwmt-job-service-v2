package uk.gov.ons.fwmt.job_service_v2.QueueReceiver;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import uk.gov.ons.fwmt.fwmtgatewaycommon.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.job_service_v2.service.TMJobConverterService;

import java.util.concurrent.CountDownLatch;

@RabbitListener
public class RMJobCreate {
    private CountDownLatch latch = new CountDownLatch(1);
    private TMJobConverterService jobService;

    @RabbitHandler
    public void receiveMessage(String message) {
        System.out.println("Received create job request <" + message + ">");
        FWMTCreateJobRequest ingest = new FWMTCreateJobRequest();
        ingest.setJobIdentity(message);
        jobService.createJob(, "");
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
