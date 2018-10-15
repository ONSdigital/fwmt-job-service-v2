package uk.gov.ons.fwmt.job_service_v2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCancelJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;
import uk.gov.ons.fwmt.fwmtohsjobstatusnotification.FwmtOHSJobStatusNotification;
import uk.gov.ons.fwmt.job_service_v2.rmproducer.RMProducer;
import uk.gov.ons.fwmt.job_service_v2.service.JobService;
import uk.gov.ons.fwmt.job_service_v2.service.tm.Impl.TMJobServiceImpl;

@Service
public class JobServiceImpl implements JobService {
  @Autowired
  private TMJobServiceImpl tmJobService;

  @Autowired
  private RMProducer rmProducer;

  @Override public void createJob(FWMTCreateJobRequest jobRequest) throws CTPException{
    tmJobService.createJob(jobRequest);
  }

  @Override public void cancelJob(FWMTCancelJobRequest cancelRequest) {
    tmJobService.cancelJob(cancelRequest);
  }

  @Override public void notifyRM(FwmtOHSJobStatusNotification response) throws CTPException {

    rmProducer.send(response);

  }
}
