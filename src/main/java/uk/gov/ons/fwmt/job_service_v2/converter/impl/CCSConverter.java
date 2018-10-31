package uk.gov.ons.fwmt.job_service_v2.converter.impl;

import com.consiliumtechnologies.schemas.mobile._2015._05.optimisemessages.CreateJobRequest;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.ResourceIdentityType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.job_service_v2.converter.TMConverter;
import uk.gov.ons.fwmt.job_service_v2.utils.CreateJobBuilder;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.time.ZoneId;

@Component("CCS")
public class CCSConverter implements TMConverter {

  private static final String DESCRIPTION = "CCS";
  private static final String WORK_TYPE = "CCS";
  private static final String SKILL = "CCS";
  private static final String ADDITIONAL_PROPERTY_CCS_ADDR_POSTCODE = "CCS_AddrPostcode";

  @Value("${totalmobile.default_world}")
  private String defaultWorld;

  @Value("${totalmobile.modworld}")
  private String modWorld;

  @Value("${fwmt.workTypes.ccs.duration}")
  private int duration;

  private DatatypeFactory datatypeFactory;

  public CCSConverter() throws DatatypeConfigurationException {
    datatypeFactory = DatatypeFactory.newInstance();
  }

  @Override
  public CreateJobRequest convert(FWMTCreateJobRequest ingest) {
    CreateJobBuilder builder = new CreateJobBuilder(datatypeFactory)
        .withDescription(DESCRIPTION)
        .withWorkType(WORK_TYPE)
        .withDescription("Census - " + ingest.getAddress().getPostCode())
        .withDuration(duration)
        .withVisitComplete(false)
        .withEmergency(false)
        .withDispatched(false)
        .withAppointmentPending(false)
        .addSkill(SKILL)
        .withIdentity(ingest.getJobIdentity())
        .withDueDate(ingest.getDueDate().atTime(23, 59, 59).atZone(ZoneId.of("UTC")))
        .withContactName(ingest.getAddress().getPostCode())
        .withPostCode(ingest.getAddress().getPostCode())
        .withAdditionalProperties(ingest.getAdditionalProperties())
        .withAdditionalProperty(ADDITIONAL_PROPERTY_CCS_ADDR_POSTCODE, ingest.getAddress().getPostCode());

    if (ingest.isPreallocatedJob()) {
      builder.withWorld(defaultWorld)
          .withAllocatedUser("test");
    } else {
      builder.withWorld(modWorld);
    }

    return builder.build();
  }

}
