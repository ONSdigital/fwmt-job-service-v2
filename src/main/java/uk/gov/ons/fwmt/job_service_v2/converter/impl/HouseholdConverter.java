package uk.gov.ons.fwmt.job_service_v2.converter.impl;

import com.consiliumtechnologies.schemas.mobile._2015._05.optimisemessages.CreateJobRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;
import uk.gov.ons.fwmt.job_service_v2.converter.TMConverter;
import uk.gov.ons.fwmt.job_service_v2.utils.CreateJobBuilder;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.time.ZoneId;

@Component("HH")
public class HouseholdConverter implements TMConverter {

  private static final String WORK_TYPE = "HH";
  private static final String DESCRIPTION = "TEST MESSAGE";
  private static final String SKILL = "Survey";

  @Value("${totalmobile.default_world}")
  private String defaultWorld;

  @Value("${totalmobile.modworld}")
  private String modWorld;

  @Value("${fwmt.workTypes.hh.duration}")
  private int duration;

  private DatatypeFactory datatypeFactory;

  public HouseholdConverter() throws CTPException {
    try {
      datatypeFactory = DatatypeFactory.newInstance();
    } catch (DatatypeConfigurationException e) {
      throw new CTPException(CTPException.Fault.SYSTEM_ERROR, e);
    }
  }

  public HouseholdConverter(String defaultWorld, String modWorld, int duration) throws CTPException {
    this();
    this.defaultWorld = defaultWorld;
    this.modWorld = modWorld;
    this.duration = duration;
  }

  @Override
  public CreateJobRequest convert(FWMTCreateJobRequest ingest) {
    return new CreateJobBuilder(datatypeFactory)
        .withDescription(DESCRIPTION)
        .withWorkType(WORK_TYPE)
        .withDuration(duration)
        .withWorld(modWorld)
        .withVisitComplete(false)
        .withEmergency(false)
        .withDispatched(false)
        .withAppointmentPending(false)
        .addSkill(SKILL)
        .withIdentity(ingest.getJobIdentity())
        .withDueDate(ingest.getDueDate().atTime(23, 59, 59).atZone(ZoneId.of("UTC")))
        .withContactName(ingest.getAddress().getPostCode())
        .withAddressLines(
            ingest.getAddress().getLine1(),
            ingest.getAddress().getLine2(),
            ingest.getAddress().getLine3(),
            ingest.getAddress().getLine4(),
            ingest.getAddress().getTownName()
        )
        .withPostCode(ingest.getAddress().getPostCode())
        .withGeoCoords(ingest.getAddress().getLongitude(), ingest.getAddress().getLatitude())
        .withAdditionalProperties(ingest.getAdditionalProperties())
        .build();
  }

}
