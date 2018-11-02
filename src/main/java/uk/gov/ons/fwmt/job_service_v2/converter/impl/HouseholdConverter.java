package uk.gov.ons.fwmt.job_service_v2.converter.impl;

import com.consiliumtechnologies.schemas.mobile._2009._03.visitstypes.AdditionalPropertyCollectionType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisemessages.CreateJobRequest;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.AddressDetailType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.ContactInfoType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.JobIdentityType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.JobType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.LocationType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.SkillCollectionType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.WorldIdentityType;
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

import static uk.gov.ons.fwmt.job_service_v2.utils.TMJobConverter.addAddressLines;
import static uk.gov.ons.fwmt.job_service_v2.utils.TMJobConverter.checkNumberOfAddressLines;

@Component("HH")
public class HouseholdConverter implements TMConverter {

    private static final String WORK_TYPE = "HH";
    private static final String DESCRIPTION = "TEST MESSAGE";

    @Value("${totalmobile.modworld}")
    private String MOD_WORLD;

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

    @Override
    public CreateJobRequest convert(FWMTCreateJobRequest ingest) {
        CreateJobRequest request = new CreateJobRequest();
        JobType job = new JobType();
        request.setJob(job);

        job.setDescription(DESCRIPTION);
        job.setDuration(duration);
        job.setWorkType(WORK_TYPE);
        job.setVisitComplete(false);
        job.setDispatched(false);
        job.setAppointmentPending(false);
        job.setEmergency(false);

        job.setIdentity(new JobIdentityType());
        job.getIdentity().setReference(ingest.getJobIdentity());

        job.setContact(new ContactInfoType());
        job.getContact().setName(ingest.getAddress().getPostCode());

        job.setSkills(new SkillCollectionType());
        job.getSkills().getSkill().add("Survey");

        job.setWorld(new WorldIdentityType());
        job.getWorld().setReference(MOD_WORLD);

        LocationType location = new LocationType();
        job.setLocation(location);
        AddressDetailType addressDetail = new AddressDetailType();
        location.setAddressDetail(addressDetail);
        addressDetail.setLines(new AddressDetailType.Lines());
        addressDetail.setPostCode(ingest.getAddress().getPostCode());
        List<String> addressLines = addressDetail.getLines().getAddressLine();
        addAddressLines(addressLines, ingest.getAddress().getLine1());
        addAddressLines(addressLines, ingest.getAddress().getLine2());
        addAddressLines(addressLines, ingest.getAddress().getLine3());
        addAddressLines(addressLines, ingest.getAddress().getLine4());
        addAddressLines(addressLines, ingest.getAddress().getTownName());
        checkNumberOfAddressLines(addressLines);

        job.setAdditionalProperties(new AdditionalPropertyCollectionType());

        GregorianCalendar dueDateCalendar = GregorianCalendar
                .from(ingest.getDueDate().atTime(23, 59, 59).atZone(ZoneId.of("UTC")));
        job.setDueDate(datatypeFactory.newXMLGregorianCalendar(dueDateCalendar));

        return request;
    }

}
