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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.job_service_v2.converter.TMConverter;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.time.ZoneId;
import java.util.GregorianCalendar;

import static uk.gov.ons.fwmt.job_service_v2.utils.TMJobConverter.addAdditionalProperty;

@Component("CCS")
public class CCSConverter implements TMConverter {

    private static final String WORK_TYPE = "CCS";
    private static final String SKILL = "CCS";
    private static final String ADDITIONAL_PROPERTY_CCS_ADDR_POSTCODE = "CCS_AddrPostcode";
    private static final String DEFAULT_WORLD = "Default";

    @Value("${totalmobile.modworld}")
    private String MOD_WORLD;

    @Value("${fwmt.workTypes.ccs.duration}")
    private int duration;

    private DatatypeFactory datatypeFactory;

    public CCSConverter() throws DatatypeConfigurationException {
        datatypeFactory = DatatypeFactory.newInstance();
    }

    @Override
    public CreateJobRequest convert(FWMTCreateJobRequest ingest) {
        CreateJobRequest request = new CreateJobRequest();
        JobType job = new JobType();
        job.setIdentity(new JobIdentityType());
        job.setSkills(new SkillCollectionType());
        job.setContact(new ContactInfoType());
        job.setWorld(new WorldIdentityType());

        job.setDescription("Census - " + ingest.getAddress().getPostCode());

        job.getIdentity().setReference(ingest.getJobIdentity());
        job.getContact().setName(ingest.getAddress().getPostCode());
        job.getSkills().getSkill().add(SKILL);
        job.setWorkType(WORK_TYPE);

        if (ingest.isPreallocatedJob()) {
            job.getWorld().setReference(DEFAULT_WORLD);
            job.setAllocatedTo(new ResourceIdentityType());
            job.getAllocatedTo().setUsername("test"); //lookup not defined yet
        }
        job.getWorld().setReference(MOD_WORLD);

        job.setLocation(new LocationType());
        job.getLocation().setAddressDetail(new AddressDetailType());
        job.getLocation().getAddressDetail().setLines(new AddressDetailType.Lines());

        job.setAdditionalProperties(new AdditionalPropertyCollectionType());
        addAdditionalProperty(job, ADDITIONAL_PROPERTY_CCS_ADDR_POSTCODE, ingest.getAddress().getPostCode());

        job.getLocation().getAddressDetail().setPostCode(ingest.getAddress().getPostCode());

        GregorianCalendar dueDateCalendar = GregorianCalendar
                .from(ingest.getDueDate().atTime(23, 59, 59).atZone(ZoneId.of("UTC")));
        job.setDueDate(datatypeFactory.newXMLGregorianCalendar(dueDateCalendar));

        job.setDuration(duration);
        job.setVisitComplete(false);
        job.setDispatched(false);
        job.setAppointmentPending(false);
        job.setEmergency(false);
        request.setJob(job);

        return request;
    }

}
