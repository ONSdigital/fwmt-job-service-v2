package uk.gov.ons.fwmt.job_service_v2.tests;

import com.consiliumtechnologies.schemas.mobile._2009._03.visitstypes.VisitIdentityType;
import com.consiliumtechnologies.schemas.mobile._2009._09.compositemessages.CompositeVisitRequest;
import com.consiliumtechnologies.schemas.mobile._2009._09.compositemessages.ObjectFactory;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendMessageResponse;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.WebServiceAdapterOutputRequest;
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
import uk.gov.ons.fwmt.fwmtohsjobstatusnotification.FwmtOHSJobStatusNotification;
import uk.gov.ons.fwmt.job_service_v2.IntegrationTestConfig;
import uk.gov.ons.fwmt.job_service_v2.controller.tm_endpoint.GenericOutgoingWs;
import uk.gov.ons.fwmt.job_service_v2.controller.tm_endpoint.OutgoingWs;
import uk.gov.ons.fwmt.job_service_v2.helper.TestReceiver;
import uk.gov.ons.fwmt.noncontactdetail.NonContactDetail;
import uk.gov.ons.fwmt.propertydetails.PropertyDetails;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

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

  @Autowired GenericOutgoingWs genericOutgoingWs;

  private final String CONTENT = "<![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?><element xmlns:ns1=\"http://ons.gov.uk/fwmt/NonContactDetail\" xmlns:ns2=\"http://ons.gov.uk/fwmt/PropertyDetails\" xmlns:ns6=\"http://schemas.consiliumtechnologies.com/mobile/2009/07/FormsTypes.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"file:///C:/Users/shawn.green/OneDrive%20-%20Totalmobile/Documents/Customers/ONS/XSLT/Gateway%20Outbound%20-%2019-09-2018/fwmtOHSJobStatusNotification.xsd\">    <eventDate>2018-10-01T14:26:42.067</eventDate>    <jobIdentity>24aa4057</jobIdentity><nonContactDetail/>    <propertyDetails>        <description>Entry phone access intercom</description>    </propertyDetails>    <username>ohstestuser1</username></element>]]>";

  private final String EXPECTED = "{\"elligabilityUnknownReasonRef\":null,\"eventDate\":\"2018-10-01T13:26:42.067+0000\",\"inelligableReasonRef\":null,\"jobIdentity\":\"24aa4057\",\"nonContactDetail\":{\"contactCardLeft\":null,\"contactDateTime\":null,\"name\":null},\"outcomeCategory\":null,\"outcomeReason\":null,\"propertyDetails\":{\"description\":\"Entry phone access intercom\",\"floor\":null,\"type\":null},\"username\":\"ohstestuser1\",\"additionalProperties\":[]}";
  @Test
  public void testPathFromTMToAdapterViaJobSvc() throws Exception {

    WebServiceAdapterOutputRequest webServiceAdapterOutputRequest =  new WebServiceAdapterOutputRequest();
    webServiceAdapterOutputRequest.setContent(CONTENT);

    QName qname = new QName("request");
    JAXBElement<WebServiceAdapterOutputRequest> jaxbElement = new JAXBElement<WebServiceAdapterOutputRequest>(qname,
        WebServiceAdapterOutputRequest.class, webServiceAdapterOutputRequest);

    TestReceiver testReceiver = new TestReceiver();
    testReceiver.init();

    genericOutgoingWs.request(jaxbElement);
    //TODO work out why this wont send request to our OutgoingWs
    //    webServiceTemplate
    //        .marshalSendAndReceive("http://localhost:9999/jobs/ws", compositeVisitRequestJAXBElement);

    Thread.sleep(2000);
    assertEquals(EXPECTED, TestReceiver.result);
    assertEquals(1, TestReceiver.counter);

  }
}
