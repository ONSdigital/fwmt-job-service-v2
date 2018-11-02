package uk.gov.ons.fwmt.job_service_v2.converter.impl;

import com.consiliumtechnologies.schemas.mobile._2015._05.optimisemessages.CreateJobRequest;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;
import uk.gov.ons.fwmt.job_service_v2.converter.TMConverter;
import uk.gov.ons.fwmt.job_service_v2.utils.CreateJobBuilder;
import uk.gov.ons.fwmt.job_service_v2.utils.TMJobConverter;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.time.ZoneId;

@Component("LMS")
public class OHSConverter implements TMConverter {
  @Value("${totalmobile.default_world}")
  private String defaultWorld;

  @Value("${totalmobile.modworld}")
  private String modWorld;

  @Value("${fwmt.workTypes.ohs.duration}")
  private int duration;

  private DatatypeFactory datatypeFactory;

  public OHSConverter() throws CTPException {
    try {
      datatypeFactory = DatatypeFactory.newInstance();
    } catch (DatatypeConfigurationException e) {
      throw new CTPException(CTPException.Fault.SYSTEM_ERROR, e);
    }
  }

  public OHSConverter(String defaultWorld, String modWorld, int duration) throws CTPException {
    this();
    this.defaultWorld = defaultWorld;
    this.modWorld = modWorld;
    this.duration = duration;
  }

  @Override
  public SendCreateJobRequestMessage convert(FWMTCreateJobRequest ingest) {
    CreateJobBuilder builder = new CreateJobBuilder(datatypeFactory)
        .withDefaultQueue()
        .withKey(ingest.getJobIdentity())
        .withWorkType("OHS")
        .withDescription("OHS")
        .withDuration(duration)
        .withVisitComplete(false)
        .withEmergency(false)
        .withDispatched(false)
        .withAppointmentPending(false)
        .addSkill("OHS")
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
        .withAdditionalProperty("wave", "1")
        .withAdditionalProperty("TLA", "OHS");

    if (ingest.isPreallocatedJob()) {
      builder.withWorld(defaultWorld);
      // TODO lookup not defined yet
      if (StringUtils.isNotBlank(ingest.getMandatoryResourceAuthNo())) {
        builder.withAllocatedUser("test");
      }
    } else {
      builder.withWorld(modWorld);
      // TODO lookup not defined yet
      if (StringUtils.isNotBlank(ingest.getMandatoryResourceAuthNo())) {
        builder.withAllocatedUser("temp");
      }
    }

    return builder.build();
  }
}