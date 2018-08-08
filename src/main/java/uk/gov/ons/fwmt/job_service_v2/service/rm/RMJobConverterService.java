package uk.gov.ons.fwmt.job_service_v2.service.rm;

import com.consiliumtechnologies.schemas.mobile._2009._09.compositemessages.CompositeVisitRequest;

import javax.xml.bind.JAXBElement;

public interface RMJobConverterService {
  void transformRequest(JAXBElement<CompositeVisitRequest> request) throws Exception;
}
