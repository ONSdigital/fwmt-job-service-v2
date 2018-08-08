package uk.gov.ons.fwmt.job_service_v2.service;

import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendUpdateJobHeaderRequestMessage;
import uk.gov.ons.fwmt.fwmtgatewaycommon.FWMTCreateJobRequest;

public interface TMJobConverterService {
  void convertMessageFromQueue (String message);
  SendCreateJobRequestMessage createJob(FWMTCreateJobRequest ingest, String username);
}

