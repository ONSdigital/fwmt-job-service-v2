package uk.gov.ons.fwmt.job_service_v2.converter.impl;

import com.consiliumtechnologies.schemas.mobile._2009._03.visitstypes.AdditionalPropertyCollectionType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisemessages.CreateJobRequest;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.*;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;
import uk.gov.ons.fwmt.job_service_v2.converter.TMConverter;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.time.ZoneId;
import java.util.GregorianCalendar;
import java.util.List;

import static uk.gov.ons.fwmt.job_service_v2.utils.TMJobConverter.addAdditionalProperty;
import static uk.gov.ons.fwmt.job_service_v2.utils.TMJobConverter.addAddressLines;
import static uk.gov.ons.fwmt.job_service_v2.utils.TMJobConverter.checkNumberOfAddressLines;

public class CEConverter implements TMConverter {

    private static final String WORLD = "???";
    private static final String WORK_TYPE = "CE";
    private static final String SKILL = "CE";
    private static final String DESCRIPTION = "CE";
    private static final String DEFAULT_WORLD = "Default";
    private static final int DURATION = 15;

    private DatatypeFactory datatypeFactory;
    private ObjectFactory objectFactory;

    public CEConverter() throws DatatypeConfigurationException {
        datatypeFactory = DatatypeFactory.newInstance();
        objectFactory = new ObjectFactory();
    }

    @Override
    public CreateJobRequest convert(FWMTCreateJobRequest ingest) throws CTPException {
        // root object
        CreateJobRequest request = new CreateJobRequest();
        JobType job = new JobType();
        request.setJob(job);

        // simple details
        job.setWorkType(WORK_TYPE);
        job.setDescription(DESCRIPTION);
        job.setDuration(DURATION);
        job.setVisitComplete(false);
        job.setEmergency(false);
        job.setDispatched(false);
        job.setAppointmentPending(false);
        job.setDescription(ingest.getAddress().getOrganizationName());

        // world
        job.setWorld(new WorldIdentityType());
        job.getWorld().setReference(WORLD);

        // skills
        job.setSkills(new SkillCollectionType());
        job.getSkills().getSkill().add(SKILL);

        // identity
        job.setIdentity(new JobIdentityType());
        job.getIdentity().setReference(ingest.getJobIdentity());

        // due date
        GregorianCalendar dueDateCalendar = GregorianCalendar.from(ingest.getDueDate().atTime(23, 59, 59).atZone(ZoneId.of("UTC")));
        job.setDueDate(datatypeFactory.newXMLGregorianCalendar(dueDateCalendar));

        // contact information
        job.setContact(new ContactInfoType());
        job.getContact().setName(ingest.getContact().getForename() + " " + ingest.getContact().getSurname());
        job.getContact().setWorkPhone(ingest.getContact().getPhoneNumber());
        job.getContact().setEmail(ingest.getContact().getEmail());

        // address information
        job.getLocation().setAddressDetail(new AddressDetailType());
        job.getLocation().getAddressDetail().setLines(new AddressDetailType.Lines());
        List<String> addressLines = job.getLocation().getAddressDetail().getLines().getAddressLine();
        addAddressLines(addressLines, ingest.getAddress().getLine1());
        addAddressLines(addressLines, ingest.getAddress().getLine2());
        addAddressLines(addressLines, ingest.getAddress().getLine3());
        addAddressLines(addressLines, ingest.getAddress().getLine4());
        addAddressLines(addressLines, ingest.getAddress().getTownName());
        checkNumberOfAddressLines(addressLines);
        job.getLocation().getAddressDetail().setPostCode(ingest.getAddress().getPostCode());
        if (ingest.getAddress().getLongitude() != null) {
            float geoX = ingest.getAddress().getLongitude().floatValue();
            job.getLocation().getAddressDetail().setGeoX(objectFactory.createAddressDetailTypeGeoX(geoX));
        }
        if (ingest.getAddress().getLatitude() != null) {
            float geoY = ingest.getAddress().getLatitude().floatValue();
            job.getLocation().getAddressDetail().setGeoY(objectFactory.createAddressDetailTypeGeoY(geoY));
        }

        // additional properties
        job.setAdditionalProperties(new AdditionalPropertyCollectionType());
        addAdditionalProperty(job, "EstablishmentType", ingest.getAdditionalProperties().get("EstablishmentType"));
        addAdditionalProperty(job, "Category", ingest.getAdditionalProperties().get("Category"));
        addAdditionalProperty(job, "LAD", ingest.getAdditionalProperties().get("LAD"));
        addAdditionalProperty(job, "Region", ingest.getAdditionalProperties().get("Region"));
        addAdditionalProperty(job, "CaseId", ingest.getAdditionalProperties().get("CaseId"));
        addAdditionalProperty(job, "CaseRef", ingest.getJobIdentity());

        return request;
    }
}
