package uk.gov.ons.fwmt.job_service_v2.queuereceiver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.ons.fwmt.job_service_v2.common.error.CTPException;
import uk.gov.ons.fwmt.job_service_v2.service.JobService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

@RunWith(MockitoJUnitRunner.class)
public class JobServiceMessageReceiverTest {

  @InjectMocks
  JobServiceMessageReceiver messageReceiver;

  @Mock
  private JobService jobService;

  @Mock
  private ObjectMapper mapper;

  @Test
  public void receiveMessageCreate()
      throws CTPException {
    JSONObject json = new JSONObject();
    JSONObject address = new JSONObject();
    json.put("actionType", "Create");
    json.put("jobIdentity", "1234");
    json.put("surveyType", "HH");
    json.put("preallocatedJob", "true");
    json.put("mandatoryResourceAuthNo", "1234");
    json.put("dueDate", "20180216");
    address.put("line1", "886");
    address.put("line2", "Prairie Rose");
    address.put("line3", "Trail");
    address.put("line4", "RU");
    address.put("townName", "Borodinskiy");
    address.put("postCode", "188961");
    address.put("latitude", "61.7921776");
    address.put("longitude", "34.3739957");
    json.put("address", address);

    String message = json.toString();

    messageReceiver.receiveMessage(message);

    Mockito.verify(jobService).createJob(any());
    Mockito.verify(jobService, never()).cancelJob(any());
  }

  @Test
  public void receiveMessageCancel()
      throws CTPException {
    JSONObject json = new JSONObject();
    json.put("actionType", "Cancel");
    json.put("jobIdentity", "1234");
    json.put("reason", "incorrect address");

    String message;
    message = json.toString();

    messageReceiver.receiveMessage(message);

    Mockito.verify(jobService, never()).createJob(any());
    Mockito.verify(jobService).cancelJob(any());
  }

}
