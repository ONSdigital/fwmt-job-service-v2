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

@Component("CE")
public class CEConverter implements TMConverter {

  private static final String WORK_TYPE = "CE";
  private static final String SKILL = "CE";
  private static final String DESCRIPTION = "CE";

  private DatatypeFactory datatypeFactory;

  @Value("${totalmobile.modworld}")
  private String modWorld;

  @Value("${fwmt.workTypes.ce.duration}")
  private int duration;

  public CEConverter() throws DatatypeConfigurationException {
    datatypeFactory = DatatypeFactory.newInstance();
  }

  @Override
  public CreateJobRequest convert(FWMTCreateJobRequest ingest) throws CTPException {
    return new CreateJobBuilder(datatypeFactory)
        .withDescription(DESCRIPTION)
        .withWorkType(WORK_TYPE)
        .withDescription(ingest.getAddress().getOrganisationName())
        .withDuration(duration)
        .withWorld(modWorld)
        .withVisitComplete(false)
        .withEmergency(false)
        .withDispatched(false)
        .withAppointmentPending(false)
        .addSkill(SKILL)
        .withIdentity(ingest.getJobIdentity())
        .withDueDate(ingest.getDueDate().atTime(23, 59, 59).atZone(ZoneId.of("UTC")))
        .withContactName(ingest.getContact().getForename() + " " + ingest.getContact().getSurname())
        .withContactEmail(ingest.getContact().getEmail())
        .withContactPhone(ingest.getContact().getPhoneNumber())
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
        .withAdditionalProperty("CaseRef", ingest.getJobIdentity())
        .build();
  }
}
