package uk.gov.ons.fwmt.job_service_v2.common.retry;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.retry.interceptor.MethodInvocationRecoverer;

public class CustomMessageRecover implements MethodInvocationRecoverer<Void> {

  @Override
  public Void recover(Object[] args, Throwable cause) {
    throw new AmqpRejectAndDontRequeueException(cause);
  }
}
