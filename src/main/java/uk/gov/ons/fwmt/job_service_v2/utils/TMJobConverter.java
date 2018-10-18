package uk.gov.ons.fwmt.job_service_v2.utils;

import com.consiliumtechnologies.schemas.mobile._2009._03.visitstypes.AdditionalPropertyType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisemessages.CreateJobRequest;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisemessages.DeleteJobRequest;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.AuditType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.JobIdentityType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.JobType;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendDeleteJobRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendMessageRequestInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;
import uk.gov.ons.fwmt.job_service_v2.converter.TMConverter;

import java.util.List;
import java.util.Map;

@Slf4j
public final class TMJobConverter {
  protected static final String JOB_QUEUE = "\\OPTIMISE\\INPUT";

  public static SendCreateJobRequestMessage createJob(FWMTCreateJobRequest ingest,
      TMConverter tmConverter) throws CTPException {

    CreateJobRequest request = createJobRequestFromIngest(ingest, tmConverter);

    SendCreateJobRequestMessage message = new SendCreateJobRequestMessage();
    message.setSendMessageRequestInfo(makeSendMessageRequestInfo(ingest.getJobIdentity()));
    message.setCreateJobRequest(request);

    return message;
  }

  public static SendDeleteJobRequestMessage deleteJob(String jobIdentity, String deletionReason,
      String TMAdminUsername) {
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

  protected static CreateJobRequest createJobRequestFromIngest(FWMTCreateJobRequest ingest,
      TMConverter tmConverter) throws CTPException {
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

  public static void addAdditionalProperty(JobType job, String key, String value) {
    AdditionalPropertyType propertyType = new AdditionalPropertyType();
    propertyType.setName(key);
    propertyType.setValue(value);
    job.getAdditionalProperties().getAdditionalProperty().add(propertyType);
  }

  public static void addAdditionalPropertiesFromMap(JobType job, Map<String, String> commonProperties) {
    AdditionalPropertyType propertyType = new AdditionalPropertyType();

    for (Map.Entry<String, String> stringEntry : commonProperties.entrySet()) {
      propertyType.setName(stringEntry.getKey());
      propertyType.setValue(stringEntry.getValue());
      job.getAdditionalProperties().getAdditionalProperty().add(propertyType);
    }
  }
}

