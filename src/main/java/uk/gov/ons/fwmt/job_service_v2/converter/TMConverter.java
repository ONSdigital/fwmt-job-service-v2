package uk.gov.ons.fwmt.job_service_v2.converter;

import com.consiliumtechnologies.schemas.mobile._2015._05.optimisemessages.CreateJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;

public interface TMConverter {
  CreateJobRequest convert(FWMTCreateJobRequest ingest);

}
