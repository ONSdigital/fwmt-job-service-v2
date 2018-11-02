package uk.gov.ons.fwmt.job_service_v2.utils;

import com.consiliumtechnologies.schemas.mobile._2015._05.optimisemessages.DeleteJobRequest;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.AuditType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.JobIdentityType;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendDeleteJobRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendMessageRequestInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Slf4j
public final class TMJobConverter {
  public static final String JOB_QUEUE = "\\OPTIMISE\\INPUT";

  public static SendDeleteJobRequestMessage deleteJob(String jobIdentity, String deletionReason,
      String tmAdminUsername) {
    SendDeleteJobRequestMessage message = new SendDeleteJobRequestMessage();
    DeleteJobRequest deleteJobRequest = new DeleteJobRequest();
    JobIdentityType jobIdentityType = new JobIdentityType();
    AuditType auditType = new AuditType();

    jobIdentityType.setReference(jobIdentity);
    deleteJobRequest.setIdentity(jobIdentityType);
    deleteJobRequest.setDeletionReason(deletionReason);
    auditType.setUsername(tmAdminUsername);
    deleteJobRequest.setDeletedBy(auditType);

    message.setSendMessageRequestInfo(makeSendMessageRequestInfo(jobIdentity));
    message.setDeleteJobRequest(deleteJobRequest);

    return message;
  }

  public static void addAddressLines(List<String> addressLines, String addressLine) {
    if (StringUtils.isNotBlank(addressLine)) {
      addressLines.add(addressLine);
    }
  }

  protected static SendMessageRequestInfo makeSendMessageRequestInfo(String key) {
    SendMessageRequestInfo info = new SendMessageRequestInfo();
    info.setQueueName(JOB_QUEUE);
    info.setKey(key);
    return info;
  }

}

