package uk.gov.ons.fwmt.job_service_v2.service.tm;

import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessage;
import uk.gov.ons.fwmt.fwmtgatewaycommon.FWMTCreateJobRequest;

public interface TMJobConverterService {
  void convertMessageFromQueue(String message);

  SendCreateJobRequestMessage createJob(FWMTCreateJobRequest ingest, String username);

  void deleteJob(String jobID);
}

