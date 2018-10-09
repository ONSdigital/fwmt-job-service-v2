package uk.gov.ons.fwmt.job_service_v2.tests;

import com.consiliumtechnologies.schemas.mobile._2009._03.visitstypes.VisitIdentityType;
import com.consiliumtechnologies.schemas.mobile._2009._09.compositemessages.CompositeVisitRequest;
import com.consiliumtechnologies.schemas.mobile._2009._09.compositemessages.ObjectFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ws.client.core.WebServiceTemplate;
import uk.gov.ons.fwmt.job_service_v2.IntegrationTestConfig;
import uk.gov.ons.fwmt.job_service_v2.controller.tm_endpoint.OutgoingWs;
import uk.gov.ons.fwmt.job_service_v2.helper.TestReceiver;

import javax.xml.bind.JAXBElement;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Component
@Slf4j
@Import({IntegrationTestConfig.class, TestReceiver.class})
@Ignore
public class JobServiceV2Tests {

  @Autowired
  @Qualifier("testWSTemplate")
  WebServiceTemplate webServiceTemplate;

  @Autowired OutgoingWs outgoingWs;
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

    outgoingWs.sendCompositeVisitRequestOutput(compositeVisitRequestJAXBElement);
    //TODO work out why this wont send request to our OutgoingWs
    //    webServiceTemplate
    //        .marshalSendAndReceive("http://localhost:9999/jobs/ws", compositeVisitRequestJAXBElement);

    Thread.sleep(2000);
    assertEquals("{\"identity\":\"testGuid\"}", TestReceiver.result);
    assertEquals(1, TestReceiver.counter);

  }
}
