package uk.gov.ons.fwmt.job_service_v2.service;

import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.DummyTMResponse;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCancelJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;

public interface JobService {
  void createJob(FWMTCreateJobRequest jobRequest);

  void cancelJob(FWMTCancelJobRequest cancelRequest);

  void notifyRM(DummyTMResponse dummyTMResponse) throws CTPException;
}
