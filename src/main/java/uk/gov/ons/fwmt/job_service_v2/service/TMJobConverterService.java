package uk.gov.ons.fwmt.job_service_v2.service;

import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendUpdateJobHeaderRequestMessage;
import uk.gov.ons.fwmt.job_service_v2.data.RmSampleIngest;

public interface TMJobConverterService {
  SendCreateJobRequestMessage createJob(RmSampleIngest ingest, String username);
  SendUpdateJobHeaderRequestMessage updateJob(String tmJobId, String username);
  SendUpdateJobHeaderRequestMessage updateJob(RmSampleIngest ingest, String username);
  SendCreateJobRequestMessage createReissue(RmSampleIngest ingest, String username);
}

