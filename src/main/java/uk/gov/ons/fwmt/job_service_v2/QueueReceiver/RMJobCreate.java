package uk.gov.ons.fwmt.job_service_v2.QueueReceiver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.ons.fwmt.fwmtgatewaycommon.Address;
import uk.gov.ons.fwmt.fwmtgatewaycommon.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.job_service_v2.service.TMJobConverterService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;

@RabbitListener
public class RMJobCreate {
    private CountDownLatch latch = new CountDownLatch(1);

    @Autowired
    private TMJobConverterService jobService;

    @RabbitHandler
    public void receiveMessage(String message) {
        System.out.println("Received create job request <" + message + ">");
        jobService.convertMessageFromQueue(message);
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
