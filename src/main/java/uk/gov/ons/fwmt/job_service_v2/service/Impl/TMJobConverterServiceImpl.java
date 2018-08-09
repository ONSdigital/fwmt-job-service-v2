package uk.gov.ons.fwmt.job_service_v2.service.Impl;

import com.consiliumtechnologies.schemas.mobile._2009._03.visitstypes.AdditionalPropertyCollectionType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisemessages.CreateJobRequest;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.AddressDetailType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.ContactInfoType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.JobIdentityType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.JobType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.LocationType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.NameValueAttributeCollectionType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.ResourceIdentityType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.SkillCollectionType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.WorldIdentityType;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendMessageRequestInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import uk.gov.ons.fwmt.fwmtgatewaycommon.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.job_service_v2.service.TMJobConverterService;
import uk.gov.ons.fwmt.job_service_v2.service.TMService;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;
import java.util.List;

@Slf4j
@Service
public class TMJobConverterServiceImpl implements TMJobConverterService {
  protected static final String JOB_QUEUE = "\\OPTIMISE\\INPUT";
  protected static final String JOB_SKILL = "Survey";
  protected static final String JOB_WORK_TYPE = "SS";
  protected static final String JOB_WORLD = "Default";

  @Autowired
  private TMService tmService;

  private final DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();

  public TMJobConverterServiceImpl() throws DatatypeConfigurationException {
  }

  protected CreateJobRequest createJobRequestFromIngest(FWMTCreateJobRequest ingest, String username) {
    CreateJobRequest request = new CreateJobRequest();
    JobType job = new JobType();
    request.setJob(job);
    job.setLocation(new LocationType());
    job.setIdentity(new JobIdentityType());
    job.getLocation().setAddressDetail(new AddressDetailType());
    job.getLocation().getAddressDetail().setLines(new AddressDetailType.Lines());
    job.setContact(new ContactInfoType());
    job.setAttributes(new NameValueAttributeCollectionType());
    job.setAllocatedTo(new ResourceIdentityType());
    job.setSkills(new SkillCollectionType());
    job.setAdditionalProperties(new AdditionalPropertyCollectionType());
    job.setWorld(new WorldIdentityType());

    request.getJob().getIdentity().setReference(ingest.getJobIdentity());

    LocationType location = request.getJob().getLocation();
    List<String> addressLines = location.getAddressDetail().getLines().getAddressLine();

//    addAddressLines(addressLines, ingest.getAddress().getLine1());
//    addAddressLines(addressLines, ingest.getAddress().getLine2());
//    addAddressLines(addressLines, ingest.getAddress().getLine3());
//    addAddressLines(addressLines, ingest.getAddress().getLine4());
//    addAddressLines(addressLines, ingest.getAddress().getTownName());
    checkNumberOfAddressLines(addressLines);

//    location.getAddressDetail().setPostCode(ingest.getAddress().getPostCode());
    //location.setReference(ingest.getSerNo());

//    request.getJob().getContact().setName(ingest.getAddress().getPostCode());
    request.getJob().getSkills().getSkill().add(JOB_SKILL);
    request.getJob().setWorkType(JOB_WORK_TYPE);
    request.getJob().getWorld().setReference(JOB_WORLD);

//    GregorianCalendar dueDateCalendar = GregorianCalendar
//            .from(ingest.getDueDate().atTime(23, 59, 59).atZone(ZoneId.of("UTC")));
//    request.getJob().setDueDate(datatypeFactory.newXMLGregorianCalendar(dueDateCalendar));
   request.getJob().getAllocatedTo().setUsername(username);

    request.getJob().setDuration(1);
    request.getJob().setVisitComplete(false);
    request.getJob().setDispatched(false);
    request.getJob().setAppointmentPending(false);
    request.getJob().setEmergency(false);

    return request;
  }

  protected void addAddressLines(List<String> addressLines, String addressLine) {
    if (StringUtils.isNotBlank((addressLine))) {
      addressLines.add(addressLine);
    }
  }

  protected void checkNumberOfAddressLines(List<String> addressLines) {
    if (addressLines.size() == 6 ) {
      String addressConcat = addressLines.get(2) + " " + addressLines.get(3);
      addressLines.set(2, addressConcat);
      addressLines.remove(3);
    }
  }

  protected SendMessageRequestInfo makeSendMessageRequestInfo(String key) {
    SendMessageRequestInfo info = new SendMessageRequestInfo();
    info.setQueueName(JOB_QUEUE);
    info.setKey(key);
    return info;
  }

  public void convertMessageFromQueue(byte[] message) {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.BASIC_ISO_DATE));
    mapper.registerModule(module);
    FWMTCreateJobRequest ingest = new FWMTCreateJobRequest();
    try {
      ingest = mapper.readValue(message, FWMTCreateJobRequest.class);
    } catch (IOException e) {
      e.printStackTrace();
    }

    SendCreateJobRequestMessage request = this.createJob(ingest,"");
    tmService.send(request);

  }

  public SendCreateJobRequestMessage createJob(FWMTCreateJobRequest ingest, String username) {


    CreateJobRequest request = createJobRequestFromIngest(ingest, username);

    SendCreateJobRequestMessage message = new SendCreateJobRequestMessage();
    message.setSendMessageRequestInfo(makeSendMessageRequestInfo(ingest.getJobIdentity()));
    message.setCreateJobRequest(request);

    return message;
  }
}

