package uk.gov.ons.fwmt.job_service_v2.service;

import uk.gov.ons.fwmt.fwmtgatewaycommon.data.DummyTMResponse;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCancelJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;

import javax.xml.datatype.DatatypeConfigurationException;

public interface JobService {
  void createJob(FWMTCreateJobRequest jobRequest) throws DatatypeConfigurationException;

  void cancelJob(FWMTCancelJobRequest cancelRequest);

  void notifyRM(DummyTMResponse dummyTMResponse);
}
