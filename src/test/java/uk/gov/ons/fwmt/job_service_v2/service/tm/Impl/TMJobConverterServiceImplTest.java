package uk.gov.ons.fwmt.job_service_v2.service.tm.Impl;

import com.consiliumtechnologies.schemas.mobile._2015._05.optimisemessages.CreateJobRequest;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.ons.fwmt.fwmtgatewaycommon.Address;
import uk.gov.ons.fwmt.fwmtgatewaycommon.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.job_service_v2.service.tm.Impl.TMJobConverterServiceImpl;


import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class TMJobConverterServiceImplTest {

    @InjectMocks
    TMJobConverterServiceImpl tmJobConverterService;

    @Mock
    SendCreateJobRequestMessage sendCreateJobRequestMessage;

    @Test
    public void createJobRequestTest(){

        String user = "bob.smith";
        FWMTCreateJobRequest ingest = new FWMTCreateJobRequest();
        Address address = new Address();
        ingest.setJobIdentity("1234");
        ingest.setSurveyType("Create");
        ingest.setPreallocatedJob(true);
        ingest.setMandatoryResourceAuthNo("1234");
        ingest.setDueDate(LocalDate.now());
        address.setLine1("886");
        address.setLine2("Prairie Rose");
        address.setLine3("Trail");
        address.setLine4("RU");
        address.setTownName("Borodinskiy");
        address.setPostCode("188961");
        address.setLatitude(BigDecimal.valueOf(61.7921776));
        address.setLongitude(BigDecimal.valueOf(34.3739957));
        ingest.setAddress(address);

        SendCreateJobRequestMessage request = tmJobConverterService.createJob(ingest,user);

        Mockito.verify(tmJobConverterService).createJobRequestFromIngest(any(),any());
        Mockito.verify(tmJobConverterService, times(5)).addAddressLines(any(),any());
        Mockito.verify(tmJobConverterService).checkNumberOfAddressLines(any());
        Mockito.verify(sendCreateJobRequestMessage).setSendMessageRequestInfo(any());
        Mockito.verify(sendCreateJobRequestMessage).setCreateJobRequest(any());

        assertEquals(request,"");


    }

    @Test
    void addAddressLinesTest(){

    }

    @Test
    void checkNumberOfAddressLinesTest(){

    }

    @Test
    void makeSendMessageRequestInfoTest(){

    }

    @Test
    void createJobTest(){

    }

    @Test
    void deleteJobTest(){

    }
}
