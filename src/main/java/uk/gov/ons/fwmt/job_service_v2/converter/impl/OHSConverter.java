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
import uk.gov.ons.fwmt.job_service_v2.utils.TMJobConverter;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.time.ZoneId;
import java.util.GregorianCalendar;
import java.util.List;

import static uk.gov.ons.fwmt.job_service_v2.utils.TMJobConverter.addAdditionalProperty;
import static uk.gov.ons.fwmt.job_service_v2.utils.TMJobConverter.addAddressLines;
import static uk.gov.ons.fwmt.job_service_v2.utils.TMJobConverter.checkNumberOfAddressLines;

@Component("LMS")
public class OHSConverter implements TMConverter {

    private static final String DESCRIPTION = "OHS";
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

    @Value("${fwmt.workTypes.ohs.duration}")
    private int duration;

    private DatatypeFactory datatypeFactory;
    private ObjectFactory objectFactory;

    public OHSConverter() throws DatatypeConfigurationException {
        datatypeFactory = DatatypeFactory.newInstance();
        objectFactory = new ObjectFactory();
    }

    public CreateJobRequest convert(FWMTCreateJobRequest ingest) {
        CreateJobRequest createJobRequest = new CreateJobRequest();
        JobType job = new JobType();
        createJobRequest.setJob(job);

        job.setDescription(DESCRIPTION);
        job.setDuration(duration);
        job.setVisitComplete(STATUS);
        job.setEmergency(EMERGENCY);
        job.setDispatched(DISPATCHED);
        job.setAppointmentPending(APPOINTMENT_PENDING);
        job.setWorkType(WORK_TYPE);

        job.setSkills(new SkillCollectionType());
        job.getSkills().getSkill().add(SKILL);

        job.setIdentity(new JobIdentityType());
        job.getIdentity().setReference(ingest.getJobIdentity());

        job.setContact(new ContactInfoType());
        job.getContact().setName(ingest.getAddress().getPostCode());

        job.setLocation(new LocationType());
        job.getLocation().setAddressDetail(new AddressDetailType());
        job.getLocation().getAddressDetail().setLines(new AddressDetailType.Lines());
        List<String> addressLines = job.getLocation().getAddressDetail().getLines().getAddressLine();
        addAddressLines(addressLines, ingest.getAddress().getLine1());
        addAddressLines(addressLines, ingest.getAddress().getLine2());
        addAddressLines(addressLines, ingest.getAddress().getLine3());
        addAddressLines(addressLines, ingest.getAddress().getLine4());
        addAddressLines(addressLines, ingest.getAddress().getTownName());
        checkNumberOfAddressLines(addressLines);

        if (ingest.getAddress().getLongitude() != null && ingest.getAddress().getLatitude() != null) {
            job.getLocation().getAddressDetail()
                    .setGeoX(objectFactory.createAddressDetailTypeGeoX(ingest.getAddress().getLongitude().floatValue()));
            job.getLocation().getAddressDetail()
                    .setGeoY(objectFactory.createAddressDetailTypeGeoY(ingest.getAddress().getLatitude().floatValue()));
        }
        job.getLocation().getAddressDetail().setPostCode(ingest.getAddress().getPostCode());

        GregorianCalendar dueDateCalendar = GregorianCalendar
                .from(ingest.getDueDate().atTime(23, 59, 59).atZone(ZoneId.of("UTC")));
        job.setDueDate(datatypeFactory.newXMLGregorianCalendar(dueDateCalendar));

        job.setWorld(new WorldIdentityType());
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
        if (ingest.getAdditionalProperties() != null) {
            TMJobConverter.addAdditionalPropertiesFromMap(job, ingest.getAdditionalProperties());
        }
        addAdditionalProperty(job, ADDITIONAL_PROPERTY_WAVE, DEFAULT_WAVE);
        addAdditionalProperty(job, ADDITIONAL_PROPERTY_TLA, DEFAULT_TLA);
        //TODO: Map Additional properties from Adapter

        return createJobRequest;
    }
}