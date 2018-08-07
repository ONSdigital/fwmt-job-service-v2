package uk.gov.ons.fwmt.job_service_v2.service;

import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendUpdateJobHeaderRequestMessage;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleIngest;
import uk.gov.ons.fwmt.job_service_v2.data.rmSampleIngest;

public interface TMJobConverterService {
  SendCreateJobRequestMessage createJob(rmSampleIngest ingest, String username);
  SendUpdateJobHeaderRequestMessage updateJob(String tmJobId, String username);
  SendUpdateJobHeaderRequestMessage updateJob(rmSampleIngest ingest, String username);
  SendCreateJobRequestMessage createReissue(rmSampleIngest ingest, String username);
}

