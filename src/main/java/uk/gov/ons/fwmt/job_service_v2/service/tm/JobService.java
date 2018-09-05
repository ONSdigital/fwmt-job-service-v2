package uk.gov.ons.fwmt.job_service_v2.service.tm;

import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCancelJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;

import javax.xml.datatype.DatatypeConfigurationException;

public interface JobService {
  void createJob(FWMTCreateJobRequest jobRequest);

  void cancelJob(FWMTCancelJobRequest cancelRequest);
}
