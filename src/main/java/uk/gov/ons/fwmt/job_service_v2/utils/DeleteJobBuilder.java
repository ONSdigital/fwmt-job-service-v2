package uk.gov.ons.fwmt.job_service_v2.utils;

import com.consiliumtechnologies.schemas.mobile._2015._05.optimisemessages.DeleteJobRequest;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisemessages.ObjectFactory;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendDeleteJobRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendMessageRequestInfo;

import javax.xml.datatype.DatatypeFactory;

public class DeleteJobBuilder {
  public static final String DEFAULT_JOB_QUEUE = "\\OPTIMISE\\INPUT";

  SendDeleteJobRequestMessage message;
  DeleteJobRequest request;
  DatatypeFactory datatypeFactory;
  ObjectFactory objectFactory;

  public DeleteJobBuilder(DatatypeFactory datatypeFactory) {
    this.datatypeFactory = datatypeFactory;
    this.objectFactory = new ObjectFactory();

    setup();
  }

  private void setup() {
    this.message = new SendDeleteJobRequestMessage();
    this.request = new DeleteJobRequest();

    this.message.setSendMessageRequestInfo(new SendMessageRequestInfo());
    this.message.setDeleteJobRequest(this.request);

    this.request.set
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

  public DeleteJobBuilder withJobIdentity() {

  }

}
