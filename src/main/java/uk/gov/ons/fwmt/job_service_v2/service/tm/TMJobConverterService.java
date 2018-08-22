package uk.gov.ons.fwmt.job_service_v2.service.tm;

import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendDeleteJobRequestMessage;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;

public interface TMJobConverterService {
  SendCreateJobRequestMessage createJob(FWMTCreateJobRequest ingest, String username);

  SendDeleteJobRequestMessage deleteJob(String jobIdentity, String deletionReason);
}

