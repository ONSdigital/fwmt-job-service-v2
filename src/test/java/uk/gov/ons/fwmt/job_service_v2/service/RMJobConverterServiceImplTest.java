package uk.gov.ons.fwmt.job_service_v2.service;

import com.consiliumtechnologies.schemas.mobile._2009._03.visitstypes.VisitIdentityType;
import com.consiliumtechnologies.schemas.mobile._2009._09.compositemessages.CompositeVisitRequest;
import com.consiliumtechnologies.schemas.mobile._2009._09.compositemessages.ObjectFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import uk.gov.ons.fwmt.job_service_v2.dto.UnknownDto;
import uk.gov.ons.fwmt.job_service_v2.rmproducer.RMProducer;
import uk.gov.ons.fwmt.job_service_v2.service.rm.impl.RMJobConverterServiceImpl;

import javax.xml.bind.JAXBElement;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class RMJobConverterServiceImplTest {

  @InjectMocks RMJobConverterServiceImpl rmJobConverterService;
  @Mock RMProducer rmProducer;
  @Captor
  ArgumentCaptor argCaptor;

  @Test
  public void transformRequest() {
    //Given
    ObjectFactory factory = new ObjectFactory();
    CompositeVisitRequest request = factory.createCompositeVisitRequest();

    VisitIdentityType visitIdentityType = new VisitIdentityType();
    visitIdentityType.setGuid("doug");
    visitIdentityType.setCompany("doug");
    visitIdentityType.setReference("doug");
    visitIdentityType.setWorkType("doug");
    request.setIdentity(visitIdentityType);

    JAXBElement<CompositeVisitRequest> input = factory.createCompositeVisitRequest(request);

    //When
    rmJobConverterService.transformRequest(input);

    //Then
    Mockito.verify(rmProducer).send((UnknownDto) argCaptor.capture());
    UnknownDto result = (UnknownDto) argCaptor.getValue();
    assertEquals(visitIdentityType.getGuid(), result.getIdentity());
  }
}