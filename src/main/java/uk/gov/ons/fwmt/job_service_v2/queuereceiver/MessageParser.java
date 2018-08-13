package uk.gov.ons.fwmt.job_service_v2.queuereceiver;

import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendDeleteJobRequestMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.fwmt.fwmtgatewaycommon.FWMTCancelJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.job_service_v2.service.tm.TMJobConverterService;
import uk.gov.ons.fwmt.job_service_v2.service.tm.TMService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class MessageParser {

  @Autowired private TMJobConverterService tmJobConverterService;
  @Autowired private TMService tmService;

  public void receiveMessage(String message) throws IllegalAccessException, InstantiationException {
    log.info("received a message: " + message);
    log.info(message);
    this.convertMessageFromQueueToDTO(message);
  }

  private void convertMessageFromQueueToDTO(String message) throws InstantiationException, IllegalAccessException {
    if (message.contains("Create")) {
      FWMTCreateJobRequest fwmtCreateJobRequest = convertMessageToDTO(FWMTCreateJobRequest.class, message);
      SendCreateJobRequestMessage createRequest = tmJobConverterService.createJob(fwmtCreateJobRequest, "");
      tmService.send(createRequest);
    }
    if (message.contains("Cancel")) {
      FWMTCancelJobRequest fwmtCancelJobRequest = convertMessageToDTO(FWMTCancelJobRequest.class, message);
      SendDeleteJobRequestMessage deleteRequest = tmJobConverterService
          .deleteJob(fwmtCancelJobRequest.getJobIdentity(), fwmtCancelJobRequest.getReason());
      tmService.send(deleteRequest);
    }
  }

  private <T> T convertMessageToDTO(Class<T> klass, String message)
      throws IllegalAccessException, InstantiationException {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.BASIC_ISO_DATE));
    mapper.registerModule(module);
    T dto = klass.newInstance();
    try {
      dto = mapper.readValue(message, klass);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return dto;
  }
}