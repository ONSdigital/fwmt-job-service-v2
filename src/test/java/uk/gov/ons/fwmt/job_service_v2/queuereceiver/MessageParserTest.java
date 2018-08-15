package uk.gov.ons.fwmt.job_service_v2.queuereceiver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import uk.gov.ons.fwmt.job_service_v2.rmproducer.RMProducer;

@RunWith(MockitoJUnitRunner.class)
public class MessageParser {


    @InjectMocks MessageParser messageParser;

    @Test
    public void receiveMessage(){
        byte message[] = new byte[] {Byte.parseByte(" ")};
    }

}
