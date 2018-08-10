package uk.gov.ons.fwmt.job_service_v2.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;

@Slf4j
@Component
public class MyClientInterceptor implements ClientInterceptor {
  @Override public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
    log.info(messageContext.getRequest().toString());
    return true;
  }

  @Override public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
    log.info(messageContext.getRequest().toString());
    return true;
  }

  @Override public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
    log.info(messageContext.getRequest().toString());
    return true;
  }

  @Override public void afterCompletion(MessageContext messageContext, Exception ex)
      throws WebServiceClientException {
    // un-used method, which must be implemented
  }
}
