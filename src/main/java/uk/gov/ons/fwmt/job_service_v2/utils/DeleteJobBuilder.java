package uk.gov.ons.fwmt.job_service_v2.utils;

import com.consiliumtechnologies.schemas.mobile._2015._05.optimisemessages.DeleteJobRequest;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.AuditType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.JobIdentityType;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendDeleteJobRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendMessageRequestInfo;

import javax.xml.datatype.DatatypeFactory;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.GregorianCalendar;

public class DeleteJobBuilder {
  private static final String DEFAULT_JOB_QUEUE = "\\OPTIMISE\\INPUT";

  private final DatatypeFactory datatypeFactory;
  private SendDeleteJobRequestMessage message;
  private DeleteJobRequest request;

  public DeleteJobBuilder(DatatypeFactory datatypeFactory) {
    this.datatypeFactory = datatypeFactory;

    setup();
  }

  private void setup() {
    this.message = new SendDeleteJobRequestMessage();
    this.request = new DeleteJobRequest();

    this.message.setSendMessageRequestInfo(new SendMessageRequestInfo());
    this.message.setDeleteJobRequest(this.request);
  }

  private void tearDown() {
    this.message = null;
    this.request = null;
  }

  public SendDeleteJobRequestMessage build() {
    SendDeleteJobRequestMessage message = this.message;
    tearDown();
    return message;
  }

  public DeleteJobBuilder withQueue(String queue) {
    message.getSendMessageRequestInfo().setQueueName(queue);
    return this;
  }

  public DeleteJobBuilder withDefaultQueue() {
    message.getSendMessageRequestInfo().setQueueName(DEFAULT_JOB_QUEUE);
    return this;
  }

  public DeleteJobBuilder withKey(String key) {
    message.getSendMessageRequestInfo().setKey(key);
    return this;
  }

  public DeleteJobBuilder withJobIdentity(String identity) {
    this.request.setIdentity(new JobIdentityType());
    this.request.getIdentity().setReference(identity);
    return this;
  }

  public DeleteJobBuilder withDeletedBy(String username, LocalDateTime dateTime) {
    this.request.setDeletedBy(new AuditType());
    this.request.getDeletedBy().setUsername(username);
    GregorianCalendar cal = GregorianCalendar.from(dateTime.atZone(ZoneId.of("UTC")));
    this.request.getDeletedBy().setDate(datatypeFactory.newXMLGregorianCalendar(cal));
    return this;
  }

  public DeleteJobBuilder withDeletionNotes(String notes) {
    this.request.setDeletionNotes(notes);
    return this;
  }

  public DeleteJobBuilder withDeletionReason(String reason) {
    this.request.setDeletionReason(reason);
    return this;
  }

}
