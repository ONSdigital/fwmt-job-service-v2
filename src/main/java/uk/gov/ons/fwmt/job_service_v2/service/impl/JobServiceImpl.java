package uk.gov.ons.fwmt.job_service_v2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.DummyTMResponse;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCancelJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.exceptions.types.FWMTCommonException;
import uk.gov.ons.fwmt.job_service_v2.rmproducer.RMProducer;
import uk.gov.ons.fwmt.job_service_v2.service.JobService;
import uk.gov.ons.fwmt.job_service_v2.service.tm.Impl.TMJobServiceImpl;

import javax.xml.datatype.DatatypeConfigurationException;
@Service
public class JobServiceImpl implements JobService {
  @Autowired
  private TMJobServiceImpl tmJobService;

  @Autowired
  private RMProducer rmProducer;

  @Retryable(FWMTCommonException.class)
  @Override public void createJob(FWMTCreateJobRequest jobRequest) throws DatatypeConfigurationException {
    tmJobService.createJob(jobRequest);
  }

  @Retryable(FWMTCommonException.class)
  @Override public void cancelJob(FWMTCancelJobRequest cancelRequest) {
    tmJobService.cancelJob(cancelRequest);
  }

  @Retryable(FWMTCommonException.class)
  @Override public void notifyRM(DummyTMResponse dummyTMResponse) {
    rmProducer.send(dummyTMResponse);
  }
}
