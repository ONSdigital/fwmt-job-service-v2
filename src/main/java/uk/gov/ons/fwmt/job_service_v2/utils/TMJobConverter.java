package uk.gov.ons.fwmt.job_service_v2.utils;

import com.consiliumtechnologies.schemas.mobile._2009._03.visitstypes.AdditionalPropertyCollectionType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisemessages.CreateJobRequest;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisemessages.DeleteJobRequest;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.AddressDetailType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.AuditType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.ContactInfoType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.JobIdentityType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.JobType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.LocationType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.SkillCollectionType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.WorldIdentityType;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendDeleteJobRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendMessageRequestInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.job_service_v2.converter.TMConverter;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.time.ZoneId;
import java.util.GregorianCalendar;
import java.util.List;

@Slf4j
public final class TMJobConverter {
  protected static final String JOB_QUEUE = "\\OPTIMISE\\INPUT";
  protected static final String JOB_SKILL = "Demo";
  protected static final String JOB_WORK_TYPE = "Household";
  protected static final String JOB_WORLD = "MOD World";

  public static  SendCreateJobRequestMessage createJob(FWMTCreateJobRequest ingest, String username,
      TMConverter tmConverter) throws DatatypeConfigurationException {

    CreateJobRequest request = createJobRequestFromIngest(ingest, username, tmConverter);

    SendCreateJobRequestMessage message = new SendCreateJobRequestMessage();
    message.setSendMessageRequestInfo(makeSendMessageRequestInfo(ingest.getJobIdentity()));
    message.setCreateJobRequest(request);

    return message;
  }

  public static  SendDeleteJobRequestMessage deleteJob(String jobIdentity, String deletionReason, String TMAdminUsername) {
    SendDeleteJobRequestMessage message = new SendDeleteJobRequestMessage();
    DeleteJobRequest deleteJobRequest = new DeleteJobRequest();
    JobIdentityType jobIdentityType = new JobIdentityType();
    AuditType auditType = new AuditType();

    jobIdentityType.setReference(jobIdentity);
    deleteJobRequest.setIdentity(jobIdentityType);
    deleteJobRequest.setDeletionReason(deletionReason);
    auditType.setUsername(TMAdminUsername);
    deleteJobRequest.setDeletedBy(auditType);

    message.setSendMessageRequestInfo(makeSendMessageRequestInfo(jobIdentity));
    message.setDeleteJobRequest(deleteJobRequest);

    return message;
  }

  protected static CreateJobRequest createJobRequestFromIngest(FWMTCreateJobRequest ingest, String username,
      TMConverter tmConverter) throws DatatypeConfigurationException {
    return tmConverter.convert(ingest);
  }

  public static void addAddressLines(List<String> addressLines, String addressLine) {
    if (StringUtils.isNotBlank((addressLine))) {
      addressLines.add(addressLine);
    }
  }

  public static void checkNumberOfAddressLines(List<String> addressLines) {
    if (addressLines.size() == 6) {
      String addressConcat = addressLines.get(2) + " " + addressLines.get(3);
      addressLines.set(2, addressConcat);
      addressLines.remove(3);
    }
  }

  protected static SendMessageRequestInfo makeSendMessageRequestInfo(String key) {
    SendMessageRequestInfo info = new SendMessageRequestInfo();
    info.setQueueName(JOB_QUEUE);
    info.setKey(key);
    return info;
  }
}

