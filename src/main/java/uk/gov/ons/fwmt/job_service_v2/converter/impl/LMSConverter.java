package uk.gov.ons.fwmt.job_service_v2.converter.impl;

import com.consiliumtechnologies.schemas.mobile._2009._03.visitstypes.AdditionalPropertyCollectionType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisemessages.CreateJobRequest;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.AddressDetailType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.ContactInfoType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.JobIdentityType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.JobType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.LocationType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.ObjectFactory;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.ResourceIdentityType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.SkillCollectionType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.WorldIdentityType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;
import uk.gov.ons.fwmt.job_service_v2.converter.TMConverter;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.time.ZoneId;
import java.util.GregorianCalendar;
import java.util.List;

import static uk.gov.ons.fwmt.job_service_v2.utils.TMJobConverter.addAdditionalProperty;
import static uk.gov.ons.fwmt.job_service_v2.utils.TMJobConverter.addAddressLines;
import static uk.gov.ons.fwmt.job_service_v2.utils.TMJobConverter.checkNumberOfAddressLines;

@Component("LMS")
public class LMSConverter implements TMConverter {

  private static final String DESCRIPTION = "OHS";
  private static final int DURATION = 0;
  private static final boolean STATUS = false;
  private static final boolean EMERGENCY = false;
  private static final boolean DISPATCHED = false;
  private static final boolean APPOINTMENT_PENDING = false;
  private static final String SKILL = "OHS";
  private static final String WORK_TYPE = "OHS";
  private static final String DEFAULT_WORLD = "Default";
  private static final String DEFAULT_WAVE = "1";
  private static final String ADDITIONAL_PROPERTY_WAVE = "wave";
  private static final String ADDITIONAL_PROPERTY_TLA = "TLA";
  private static final String DEFAULT_TLA = "OHS";

  @Value("${totalmobile.modworld}")
  private String MOD_WORLD;

  public CreateJobRequest convert(FWMTCreateJobRequest ingest) throws CTPException {
    CreateJobRequest createJobRequest = new CreateJobRequest();
    JobType job = new JobType();

    job.setIdentity(new JobIdentityType());
    job.setSkills(new SkillCollectionType());
    job.setLocation(new LocationType());
    job.setContact(new ContactInfoType());
    job.setWorld(new WorldIdentityType());

    job.getIdentity().setReference(ingest.getJobIdentity());
    job.getLocation().setAddressDetail(new AddressDetailType());
    job.getLocation().getAddressDetail().setLines(new AddressDetailType.Lines());
    List<String> addressLines = job.getLocation().getAddressDetail().getLines().getAddressLine();

    addAddressLines(addressLines, ingest.getAddress().getLine1());
    addAddressLines(addressLines, ingest.getAddress().getLine2());
    addAddressLines(addressLines, ingest.getAddress().getLine3());
    addAddressLines(addressLines, ingest.getAddress().getLine4());
    addAddressLines(addressLines, ingest.getAddress().getTownName());
    checkNumberOfAddressLines(addressLines);

    ObjectFactory locationTypeOF = new ObjectFactory();
    if (ingest.getAddress().getLongitude() != null && ingest.getAddress().getLatitude() != null) {
      job.getLocation().getAddressDetail()
          .setGeoX(locationTypeOF.createAddressDetailTypeGeoX(ingest.getAddress().getLongitude().floatValue()));
      job.getLocation().getAddressDetail()
          .setGeoY(locationTypeOF.createAddressDetailTypeGeoY(ingest.getAddress().getLatitude().floatValue()));
    }
    job.getLocation().getAddressDetail().setPostCode(ingest.getAddress().getPostCode());

    if (StringUtils.isNotBlank(ingest.getSurveyType())) {
      job.setWorkType(ingest.getSurveyType());
    } else {
      job.setWorkType(WORK_TYPE);
    }

    GregorianCalendar dueDateCalendar = GregorianCalendar
        .from(ingest.getDueDate().atTime(23, 59, 59).atZone(ZoneId.of("UTC")));
    try {
      job.setDueDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(dueDateCalendar));
    } catch (DatatypeConfigurationException e) {
      throw new CTPException(CTPException.Fault.SYSTEM_ERROR, e);
    }

    if (ingest.isPreallocatedJob()) {
      job.getWorld().setReference(DEFAULT_WORLD);
      if (StringUtils.isNotBlank(ingest.getMandatoryResourceAuthNo())) {
        job.setAllocatedTo(new ResourceIdentityType());
        job.getAllocatedTo().setUsername("test"); //lookup not defined yet
      }
    } else {
      job.getWorld().setReference(MOD_WORLD);
      if (StringUtils.isNotBlank(ingest.getMandatoryResourceAuthNo())) {
        job.setMandatoryResource(new ResourceIdentityType());
        job.getMandatoryResource().setUsername("temp");
      }
    }

    job.setAdditionalProperties(new AdditionalPropertyCollectionType());
    addAdditionalProperty(job, ADDITIONAL_PROPERTY_WAVE, DEFAULT_WAVE);
    addAdditionalProperty(job, ADDITIONAL_PROPERTY_TLA, DEFAULT_TLA);
    //TODO: Map Additional properties from Adapter

    job.getContact().setName(ingest.getAddress().getPostCode());

    job.setDescription(DESCRIPTION);
    job.setDuration(DURATION);
    job.setVisitComplete(STATUS);
    job.getSkills().getSkill().add(SKILL);
    job.setEmergency(EMERGENCY);
    job.setDispatched(DISPATCHED);
    job.setAppointmentPending(APPOINTMENT_PENDING);

    createJobRequest.setJob(job);

    return createJobRequest;
  }
}