package uk.gov.ons.fwmt.job_service_v2.controller.tm_endpoint;

import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendMessageResponse;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.WebServiceAdapterOutputRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;
import uk.gov.ons.fwmt.job_service_v2.service.JobService;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

@Slf4j
@Endpoint
public class GenericOutgoingWs {
  private static final String NAMESPACE_URI = "http://schemas.consiliumtechnologies.com/services/mobile/2009/03/messaging";

  @Autowired
  private JobService jobService;

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "request")
  @ResponsePayload
  public JAXBElement<SendMessageResponse> request(@RequestPayload JAXBElement<WebServiceAdapterOutputRequest> request) throws
      CTPException {
    SendMessageResponse smr = new SendMessageResponse();
    QName qname = new QName("request");
    JAXBElement<SendMessageResponse> jaxbElement = new JAXBElement<SendMessageResponse>(qname,
        SendMessageResponse.class, smr);
    SendMessageResponse msg = new SendMessageResponse();
    WebServiceAdapterOutputRequest value = request.getValue();
    System.out.println(value.getContent());
   // jobService.notifyRM(value.getContent());

    msg.setId(value.getId());
    jaxbElement.setValue(msg);
    log.info("Incoming message received. Id:" + value.getId());
    return jaxbElement;
  }
}