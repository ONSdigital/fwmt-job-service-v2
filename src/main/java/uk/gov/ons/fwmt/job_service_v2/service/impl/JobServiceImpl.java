package uk.gov.ons.fwmt.job_service_v2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.fwmt.fwmtgatewaycommon.config.QueueNames;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCancelJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;
import uk.gov.ons.fwmt.fwmtohsjobstatusnotification.FwmtOHSJobStatusNotification;
import uk.gov.ons.fwmt.job_service_v2.rmproducer.RMProducer;
import uk.gov.ons.fwmt.job_service_v2.service.JobService;
import uk.gov.ons.fwmt.job_service_v2.service.tm.Impl.TMJobServiceImpl;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;

@Service
public class JobServiceImpl implements JobService {
  @Autowired
  private TMJobServiceImpl tmJobService;

  @Autowired
  private RMProducer rmProducer;

  @Override public void createJob(FWMTCreateJobRequest jobRequest) throws CTPException{
    tmJobService.createJob(jobRequest);
  }

  @Override public void cancelJob(FWMTCancelJobRequest cancelRequest) {
    tmJobService.cancelJob(cancelRequest);
  }

  @Override public void notifyRM(String response) throws CTPException {

    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(FwmtOHSJobStatusNotification.class);
      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
      ByteArrayInputStream input = new ByteArrayInputStream(response.getBytes());
      JAXBElement<FwmtOHSJobStatusNotification> responseMessage = unmarshaller.unmarshal(new StreamSource(input), FwmtOHSJobStatusNotification.class);
      rmProducer.send(responseMessage.getValue());
    } catch (JAXBException e) {
      throw new CTPException(CTPException.Fault.SYSTEM_ERROR, "Failed to unmarshal XML message.", e);
    }

  }
}
