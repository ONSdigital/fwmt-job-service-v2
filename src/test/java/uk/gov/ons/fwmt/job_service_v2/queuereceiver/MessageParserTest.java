package uk.gov.ons.fwmt.job_service_v2.queuereceiver;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.ons.fwmt.job_service_v2.service.tm.TMJobConverterService;
import uk.gov.ons.fwmt.job_service_v2.service.tm.TMService;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

@RunWith(MockitoJUnitRunner.class)
public class MessageParserTest {


    @InjectMocks
    MessageParser messageParser;

    @Mock
    private TMJobConverterService tmJobConverterService;
    @Mock
    private TMService tmService;

    @Test
    public void receiveMessageCreate() throws InstantiationException, IllegalAccessException {

        JSONObject json = new JSONObject();
        JSONObject address = new JSONObject();
        json.put("jobIdentity","1234");
        json.put("surveyType","Create");
        json.put("preallocatedJob","true");
        json.put("mandatoryResourceAuthNo","1234");
        json.put("dueDate","20180216");
        address.put("line1","886");
        address.put("line2","Prairie Rose");
        address.put("line3","Trail");
        address.put("line4","RU");
        address.put("townName","Borodinskiy");
        address.put("postCode","188961");
        address.put("latitude","61.7921776");
        address.put("longitude","34.3739957");
        json.put("address", address);

        String message = null;
        message = json.toString();
        messageParser.receiveMessage(message);

        System.out.println(json);
        System.out.println(message);

        Mockito.verify(tmJobConverterService).createJob(any(),any());
        Mockito.verify(tmJobConverterService, never()).deleteJob(any(),any());
        Mockito.verify(tmService).send(any());
    }

    @Test
    public void receiveMessageCancel() throws InstantiationException, IllegalAccessException {

        JSONObject json = new JSONObject();
        JSONObject address = new JSONObject();
        json.put("jobIdentity","1234");
        json.put("surveyType","Cancel");
        json.put("preallocatedJob","true");
        json.put("mandatoryResourceAuthNo","1234");
        json.put("dueDate","20180216");
        address.put("line1","886");
        address.put("line2","Prairie Rose");
        address.put("line3","Trail");
        address.put("line4","RU");
        address.put("townName","Borodinskiy");
        address.put("postCode","188961");
        address.put("latitude","61.7921776");
        address.put("longitude","34.3739957");
        json.put("address", address);

        String message = null;
        message = json.toString();
        messageParser.receiveMessage(message);

        System.out.println(json);
        System.out.println(message);

        Mockito.verify(tmJobConverterService, never()).createJob(any(),any());
        Mockito.verify(tmJobConverterService).deleteJob(any(),any());
        Mockito.verify(tmService).send(any());
    }

}
