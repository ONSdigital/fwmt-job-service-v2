package uk.gov.ons.fwmt.job_service_v2.converter.impl;

import com.consiliumtechnologies.schemas.mobile._2015._05.optimisemessages.CreateJobRequest;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.*;
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

public class CEConverter implements TMConverter {

    private static final String WORLD = "???";
    private static final String WORK_TYPE = "CE";
    private static final String SKILL = "CE";
    private static final String DESCRIPTION = "CE";
    private static final String DEFAULT_WORLD = "Default";
    private static final int DURATION = 15;

    private DatatypeFactory datatypeFactory;

    public CEConverter() throws DatatypeConfigurationException {
        datatypeFactory = DatatypeFactory.newInstance();
    }

    @Override
    public CreateJobRequest convert(FWMTCreateJobRequest ingest) throws CTPException {
        CreateJobRequest request = new CreateJobRequest();

        JobType job = new JobType();
        request.setJob(job);
        job.setIdentity(new JobIdentityType());
        job.setSkills(new SkillCollectionType());
        job.setContact(new ContactInfoType());
        job.setWorld(new WorldIdentityType());

        job.getIdentity().setReference(ingest.getJobIdentity());
        job.getContact().setName(ingest.getAddress().getPostCode());

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

        GregorianCalendar dueDateCalendar = GregorianCalendar.from(ingest.getDueDate().atTime(23, 59, 59).atZone(ZoneId.of("UTC")));
        job.setDueDate(datatypeFactory.newXMLGregorianCalendar(dueDateCalendar));

        job.getWorld().setReference(WORLD);
        job.getSkills().getSkill().add(SKILL);
        job.setWorkType(WORK_TYPE);
        job.setDescription(DESCRIPTION);
        job.setDuration(DURATION);
        job.setVisitComplete(false);
        job.setEmergency(false);
        job.setDispatched(false);
        job.setAppointmentPending(false);

        return request;
    }
}
