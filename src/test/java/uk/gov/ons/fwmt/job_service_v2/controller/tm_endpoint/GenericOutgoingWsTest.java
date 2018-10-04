package uk.gov.ons.fwmt.job_service_v2.controller.tm_endpoint;

import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendMessageResponse;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.WebServiceAdapterOutputRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;
import uk.gov.ons.fwmt.job_service_v2.service.JobService;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GenericOutgoingWsTest {

  @InjectMocks
  GenericOutgoingWs genericOutgoingWs;

  @Mock
  private JobService jobService;

  private final String CONTENT = "<![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?><element xmlns:ns1=\"http://ons.gov.uk/fwmt/NonContactDetail\" xmlns:ns2=\"http://ons.gov.uk/fwmt/PropertyDetails\" xmlns:ns6=\"http://schemas.consiliumtechnologies.com/mobile/2009/07/FormsTypes.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"file:///C:/Users/shawn.green/OneDrive%20-%20Totalmobile/Documents/Customers/ONS/XSLT/Gateway%20Outbound%20-%2019-09-2018/fwmtOHSJobStatusNotification.xsd\">    <eventDate>2018-10-01T14:26:42.067</eventDate>    <jobIdentity>24aa4057</jobIdentity><nonContactDetail/>    <propertyDetails>        <description>Entry phone access intercom</description>    </propertyDetails>    <username>ohstestuser1</username></element>]]>";

  @Test
  public void sendAdapterOutput() throws CTPException {
    //Given
    WebServiceAdapterOutputRequest webServiceAdapterOutputRequest = new WebServiceAdapterOutputRequest();
    webServiceAdapterOutputRequest.setContent(CONTENT);

    //When
    QName qname = new QName("request");
    JAXBElement<WebServiceAdapterOutputRequest> jaxbElement = new JAXBElement<WebServiceAdapterOutputRequest>(qname,
        WebServiceAdapterOutputRequest.class, webServiceAdapterOutputRequest);
    JAXBElement<SendMessageResponse> result = genericOutgoingWs.request(jaxbElement);

    //Then
    assertNotNull(result);
  }
}