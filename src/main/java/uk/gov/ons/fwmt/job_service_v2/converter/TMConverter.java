package uk.gov.ons.fwmt.job_service_v2.converter;

import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessage;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;

public interface TMConverter {
  SendCreateJobRequestMessage convert(FWMTCreateJobRequest ingest) throws CTPException;

}
