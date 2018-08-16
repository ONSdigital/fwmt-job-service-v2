package uk.gov.ons.fwmt.job_service_v2.service.tm.Impl;

import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.ons.fwmt.fwmtgatewaycommon.Address;
import uk.gov.ons.fwmt.fwmtgatewaycommon.FWMTCreateJobRequest;



import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.times;

public class TMJobConverterServiceImplTest {

    TMJobConverterServiceImpl tmJobConverterService = new TMJobConverterServiceImpl();

    @Test
    public void createJobTest(){

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


//        Mockito.verify(tmJobConverterService).createJobRequestFromIngest(any(),any());
//        Mockito.verify(tmJobConverterService, times(5)).addAddressLines(any(),any());
//        Mockito.verify(tmJobConverterService).checkNumberOfAddressLines(any());
//        Mockito.verify(sendCreateJobRequestMessage).setSendMessageRequestInfo(any());
//        Mockito.verify(sendCreateJobRequestMessage).setCreateJobRequest(any());


        assertEquals(request.getCreateJobRequest().getJob().getIdentity().getReference(),"1234");
        assertEquals(request.getCreateJobRequest().getJob().getContact().getName(),"1234");


    }

    @Test
    public void addAddressLinesTest(){

    }

    @Test
    public void checkNumberOfAddressLinesTest(){

    }

    @Test
    public void makeSendMessageRequestInfoTest(){

    }

    @Test
    public void createJobRequestFromIngestTest(){

    }

    @Test
    public void deleteJobTest(){

    }
}
