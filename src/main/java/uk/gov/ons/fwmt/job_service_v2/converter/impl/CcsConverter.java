package uk.gov.ons.fwmt.job_service_v2.converter.impl;

import com.consiliumtechnologies.schemas.mobile._2009._03.visitstypes.AdditionalPropertyCollectionType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisemessages.CreateJobRequest;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.AddressDetailType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.ContactInfoType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.JobIdentityType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.JobType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.LocationType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.ResourceIdentityType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.SkillCollectionType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.WorldIdentityType;
import org.springframework.stereotype.Component;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.job_service_v2.converter.TMConverter;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.time.ZoneId;
import java.util.GregorianCalendar;

import static uk.gov.ons.fwmt.job_service_v2.utils.TMJobConverter.addAdditionalProperty;

@Component("CCS")
public class CcsConverter implements TMConverter
{
  @Override public CreateJobRequest convert(FWMTCreateJobRequest ingest) {
    CreateJobRequest request = new CreateJobRequest();
    JobType job = new JobType();
    job.setIdentity(new JobIdentityType());
    job.setSkills(new SkillCollectionType());
    job.setContact(new ContactInfoType());
    job.setWorld(new WorldIdentityType());

    job.setDescription("Census - " + ingest.getAddress().getPostCode());

    job.getIdentity().setReference(ingest.getJobIdentity());
    job.getContact().setName(ingest.getAddress().getPostCode());
    job.getSkills().getSkill().add("CCS");
    job.setWorkType("CCS");


    if (ingest.isPreallocatedJob()) {
      job.getWorld().setReference("Default");
      job.setAllocatedTo(new ResourceIdentityType());
      job.getAllocatedTo().setUsername("test"); //todo add lookup for username
    }
    job.getWorld().setReference("MOD World");

    job.setLocation(new LocationType());
    job.getLocation().setAddressDetail(new AddressDetailType());
    job.getLocation().getAddressDetail().setLines(new AddressDetailType.Lines());

    job.setAdditionalProperties(new AdditionalPropertyCollectionType());
    addAdditionalProperty(job, "CCS_AddrPostcode", ingest.getAddress().getPostCode());

    job.getLocation().getAddressDetail().setPostCode(ingest.getAddress().getPostCode());

    GregorianCalendar dueDateCalendar = GregorianCalendar
        .from(ingest.getDueDate().atTime(23, 59, 59).atZone(ZoneId.of("UTC")));
    try {
      job.setDueDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(dueDateCalendar));
    } catch (DatatypeConfigurationException e) {
      e.printStackTrace();
      //TODO: Handle exception properly
    }

    job.setDuration(1);
    job.setVisitComplete(false);
    job.setDispatched(false);
    job.setAppointmentPending(false);
    job.setEmergency(false);
    request.setJob(job);

    return request;
  }

  @Override public String getType() {
    return "CCS";
  }
}
