package uk.gov.ons.fwmt.job_service_v2.utils;

import com.consiliumtechnologies.schemas.mobile._2009._03.visitstypes.AdditionalPropertyCollectionType;
import com.consiliumtechnologies.schemas.mobile._2009._03.visitstypes.AdditionalPropertyType;
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
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendMessageRequestInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

import javax.xml.datatype.DatatypeFactory;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

public class CreateJobBuilder {
  private static final String DEFAULT_JOB_QUEUE = "\\OPTIMISE\\INPUT";

  private SendCreateJobRequestMessage message;
  private CreateJobRequest request;
  private DatatypeFactory datatypeFactory;
  private ObjectFactory objectFactory;

  public CreateJobBuilder(DatatypeFactory datatypeFactory) {
    this.datatypeFactory = datatypeFactory;
    this.objectFactory = new ObjectFactory();

    setup();
  }

  private void setup() {
    this.message = new SendCreateJobRequestMessage();
    this.request = new CreateJobRequest();

    this.message.setSendMessageRequestInfo(new SendMessageRequestInfo());
    this.message.setCreateJobRequest(this.request);

    JobType job = new JobType();
    this.request.setJob(job);
    job.setWorld(new WorldIdentityType());
    job.setSkills(new SkillCollectionType());
    job.setIdentity(new JobIdentityType());
    job.setContact(new ContactInfoType());
    job.setLocation(new LocationType());
    job.getLocation().setAddressDetail(new AddressDetailType());
    job.getLocation().getAddressDetail().setLines(new AddressDetailType.Lines());
    job.setAdditionalProperties(new AdditionalPropertyCollectionType());
  }

  private void tearDown() {
    this.message = null;
    this.request = null;
  }

  public SendCreateJobRequestMessage build() {
    SendCreateJobRequestMessage message = this.message;
    tearDown();
    return message;
  }

  public CreateJobBuilder withQueue(String queue) {
    message.getSendMessageRequestInfo().setQueueName(queue);
    return this;
  }

  public CreateJobBuilder withDefaultQueue() {
    message.getSendMessageRequestInfo().setQueueName(DEFAULT_JOB_QUEUE);
    return this;
  }

  public CreateJobBuilder withKey(String key) {
    message.getSendMessageRequestInfo().setKey(key);
    return this;
  }

  public CreateJobBuilder withCorrelationId(String correlationId) {
    message.getSendMessageRequestInfo().setCorrelationId(correlationId);
    return this;
  }

  public CreateJobBuilder withDescription(String description) {
    request.getJob().setDescription(description);
    return this;
  }

  public CreateJobBuilder withWorkType(String workType) {
    request.getJob().setWorkType(workType);
    return this;
  }

  public CreateJobBuilder withDuration(int duration) {
    request.getJob().setDuration(duration);
    return this;
  }

  public CreateJobBuilder withVisitComplete(boolean complete) {
    request.getJob().setVisitComplete(complete);
    return this;
  }

  public CreateJobBuilder withEmergency(boolean emergency) {
    request.getJob().setEmergency(emergency);
    return this;
  }

  public CreateJobBuilder withDispatched(boolean dispatched) {
    request.getJob().setDispatched(dispatched);
    return this;
  }

  public CreateJobBuilder withAppointmentPending(boolean appointmentPending) {
    request.getJob().setAppointmentPending(appointmentPending);
    return this;
  }

  public CreateJobBuilder withWorld(String world) {
    request.getJob().getWorld().setReference(world);
    return this;
  }

  public CreateJobBuilder addSkill(String skill) {
    request.getJob().getSkills().getSkill().add(skill);
    return this;
  }

  public CreateJobBuilder withIdentity(String identity) {
    request.getJob().getIdentity().setReference(identity);
    return this;
  }

  public CreateJobBuilder withDueDate(ZonedDateTime dueDate) {
    GregorianCalendar dueDateCalendar = GregorianCalendar.from(dueDate);
    request.getJob().setDueDate(datatypeFactory.newXMLGregorianCalendar(dueDateCalendar));
    return this;
  }

  public CreateJobBuilder withContactName(String name) {
    request.getJob().getContact().setName(name);
    return this;
  }

  public CreateJobBuilder withContactPhone(String phoneNumber) {
    request.getJob().getContact().setWorkPhone(phoneNumber);
    return this;
  }

  public CreateJobBuilder withContactEmail(String email) {
    request.getJob().getContact().setEmail(email);
    return this;
  }

  public CreateJobBuilder withAllocatedUser(String username) {
    request.getJob().setAllocatedTo(new ResourceIdentityType());
    request.getJob().getAllocatedTo().setUsername(username);
    return this;
  }

  public CreateJobBuilder addAddressLine(String line) {
    List<String> requestLines = request.getJob().getLocation().getAddressDetail().getLines().getAddressLine();
    if (StringUtils.isNotBlank(line)) {
      requestLines.add(line);
    }
    return this;
  }

  public CreateJobBuilder withAddressLines(String... lines) {
    List<String> requestLines = request.getJob().getLocation().getAddressDetail().getLines().getAddressLine();
    for (String line : lines) {
      if (StringUtils.isNotBlank(line)) {
        requestLines.add(line);
      }
    }
    shrinkAddressLines();
    return this;
  }

  public CreateJobBuilder shrinkAddressLines() {
    List<String> requestLines = request.getJob().getLocation().getAddressDetail().getLines().getAddressLine();
    if (requestLines.size() == 6) {
      String addressConcat = requestLines.get(2) + " " + requestLines.get(3);
      requestLines.set(2, addressConcat);
      requestLines.remove(3);
    }
    return this;
  }

  public CreateJobBuilder withPostCode(String postCode) {
    request.getJob().getLocation().getAddressDetail().setPostCode(postCode);
    return this;
  }

  public CreateJobBuilder withGeoCoords(@Nullable BigDecimal x, @Nullable BigDecimal y) {
    if (x != null) {
      request.getJob().getLocation().getAddressDetail()
          .setGeoX(objectFactory.createAddressDetailTypeGeoX(x.floatValue()));
    }
    if (y != null) {
      request.getJob().getLocation().getAddressDetail()
          .setGeoY(objectFactory.createAddressDetailTypeGeoY(y.floatValue()));
    }
    return this;
  }

  public CreateJobBuilder withAdditionalProperty(String key, String value) {
    AdditionalPropertyType propertyType = new AdditionalPropertyType();
    propertyType.setName(key);
    propertyType.setValue(value);
    request.getJob().getAdditionalProperties().getAdditionalProperty().add(propertyType);
    return this;
  }

  public CreateJobBuilder withAdditionalProperties(Map<String, String> properties) {
    if (properties != null) {
      for (Map.Entry<String, String> stringEntry : properties.entrySet()) {
        if (stringEntry.getValue() != null) {
          AdditionalPropertyType propertyType = new AdditionalPropertyType();
          propertyType.setName(stringEntry.getKey());
          propertyType.setValue(stringEntry.getValue());
          request.getJob().getAdditionalProperties().getAdditionalProperty().add(propertyType);
        }
      }
    }
    return this;
  }
}
