package uk.gov.ons.fwmt.job_service_v2.service.rm.impl;

import com.consiliumtechnologies.schemas.mobile._2009._09.compositemessages.CompositeVisitRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.fwmt.job_service_v2.dto.UnknownDto;
import uk.gov.ons.fwmt.job_service_v2.rmproducer.RMProducer;
import uk.gov.ons.fwmt.job_service_v2.service.rm.RMJobConverterService;

import javax.xml.bind.JAXBElement;

@Slf4j
@Service
public class RMJobConverterServiceImpl implements RMJobConverterService {
  private RMProducer rmProducer;

  @Autowired public RMJobConverterServiceImpl(RMProducer rmProducer) {
    this.rmProducer = rmProducer;
  }

  public RMJobConverterServiceImpl() {}

  @Override public void transformRequest(JAXBElement<CompositeVisitRequest> request) {
    log.debug("Inside Request", request);

    UnknownDto unknownDto = new UnknownDto();

    unknownDto.setIdentity(request.getValue().getIdentity().getGuid());
    rmProducer.send(unknownDto.toString().getBytes());
  }
}
