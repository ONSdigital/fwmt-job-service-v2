package uk.gov.ons.fwmt.job_service_v2.tests;

import com.consiliumtechnologies.schemas.mobile._2009._03.visitstypes.VisitIdentityType;
import com.consiliumtechnologies.schemas.mobile._2009._09.compositemessages.CompositeVisitRequest;
import com.consiliumtechnologies.schemas.mobile._2009._09.compositemessages.ObjectFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import uk.gov.ons.fwmt.job_service_v2.IntegrationTestConfig;
import uk.gov.ons.fwmt.job_service_v2.helper.TestReceiver;

import javax.xml.bind.JAXBElement;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Component
@Slf4j
@Import({IntegrationTestConfig.class, TestReceiver.class})
public class JobServiceV2Tests {

  private final String TM_MESSAGE_JSON = "json";

  private final String TM_OUTGOING_XML = "XML";

  @Autowired
  @Qualifier("testWSTenplate")
  WebServiceTemplate webServiceTemplate;
  //
  //  @Autowired
  //  @Qualifier("testTmService")
  //  TMService tmService;

  @Test
  public void testPathFromTMToAdapterViaJobSvc() throws Exception {

    ObjectFactory factory = new ObjectFactory();

    CompositeVisitRequest compositeVisitRequest = factory.createCompositeVisitRequest();
    VisitIdentityType visitIdentityType = new VisitIdentityType();
    visitIdentityType.setGuid("testGuid");
    compositeVisitRequest.setIdentity(visitIdentityType);
    JAXBElement<CompositeVisitRequest> compositeVisitRequestJAXBElement = factory
        .createCompositeVisitRequest(compositeVisitRequest);

    TestReceiver testReceiver = new TestReceiver();
    testReceiver.init();

    webServiceTemplate
        .marshalSendAndReceive("http://localhost:9999/jobs/ws/", compositeVisitRequestJAXBElement,
            new SoapActionCallback(
                ""));

    Thread.sleep(2000);
    assertEquals(1, TestReceiver.counter);
    assertEquals("{\"identity\":\"test\"}", TestReceiver.result);
  }
}
