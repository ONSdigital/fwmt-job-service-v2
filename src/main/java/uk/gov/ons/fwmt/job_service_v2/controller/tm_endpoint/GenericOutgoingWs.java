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
import uk.gov.ons.fwmt.fwmtohsjobstatusnotification.FwmtOHSJobStatusNotification;
import uk.gov.ons.fwmt.job_service_v2.service.JobService;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.io.StringReader;

@Slf4j
@Endpoint
public class GenericOutgoingWs {
  private static final String NAMESPACE_URI = "http://schemas.consiliumtechnologies.com/services/mobile/2009/03/messaging";

  @Autowired
  private JobService jobService;

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "request")
  @ResponsePayload
  public JAXBElement<SendMessageResponse> request(@RequestPayload JAXBElement<WebServiceAdapterOutputRequest> request)
      throws
      CTPException {
    WebServiceAdapterOutputRequest value = request.getValue();
    FwmtOHSJobStatusNotification responseMessage = convertMessage(value);
    jobService.notifyRM(responseMessage);

    return createResponse(value);
  }

  private JAXBElement<SendMessageResponse> createResponse(WebServiceAdapterOutputRequest value) {
    SendMessageResponse msg = new SendMessageResponse();
    msg.setId(value.getId());

    SendMessageResponse smr = new SendMessageResponse();
    QName qname = new QName("request");
    JAXBElement<SendMessageResponse> jaxbElement = new JAXBElement<SendMessageResponse>(qname,
        SendMessageResponse.class, smr);
    jaxbElement.setValue(msg);
    log.debug("Incoming message received. Id:" + msg.getId());

    return jaxbElement;
  }

  private FwmtOHSJobStatusNotification convertMessage(WebServiceAdapterOutputRequest value) throws CTPException {
    String content = value.getContent();
    content = content.replaceAll("&lt;", "<");
    content = content.replaceAll("&gt;", ">");
    content = content.replaceAll("<!\\[CDATA\\[", "");
    content = content.replaceAll("\\]\\]>", "");

    FwmtOHSJobStatusNotification responseMessage;
    responseMessage = JAXB.unmarshal(new StringReader(content), FwmtOHSJobStatusNotification.class);

    return responseMessage;
  }
}