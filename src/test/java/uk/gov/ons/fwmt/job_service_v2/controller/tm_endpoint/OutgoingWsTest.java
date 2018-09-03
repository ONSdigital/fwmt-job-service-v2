package uk.gov.ons.fwmt.job_service_v2.controller.tm_endpoint;

import com.consiliumtechnologies.schemas.mobile._2008._10.locationmessages.SubmitLocationRequest;
import com.consiliumtechnologies.schemas.mobile._2009._03.commonmessages.SubmitDocumentRequest;
import com.consiliumtechnologies.schemas.mobile._2009._03.visitsmessages.CompleteVisitRequest;
import com.consiliumtechnologies.schemas.mobile._2009._03.visitsmessages.RequestVisitRequest;
import com.consiliumtechnologies.schemas.mobile._2009._03.visitsmessages.UpdateVisitStatusRequest;
import com.consiliumtechnologies.schemas.mobile._2009._03.visitstypes.VisitIdentityType;
import com.consiliumtechnologies.schemas.mobile._2009._07.formsmessages.SubmitFormResultRequest;
import com.consiliumtechnologies.schemas.mobile._2009._09.compositemessages.CompositeVisitRequest;
import com.consiliumtechnologies.schemas.mobile._2009._09.compositemessages.ObjectFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.ons.fwmt.job_service_v2.service.rm.RMJobConverterService;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OutgoingWsTest {

  @InjectMocks
  OutgoingWs outgoingWs;

  @Mock
  private RMJobConverterService rmJobConverterService;

  public <T> JAXBElement<T> getElement(Class<T> inputClass) {
    JAXBElement<T> result = new JAXBElement<T>(QName.valueOf(""), inputClass, null, null);
    return result;
  }

  @Test
  public void sendUpdateVisitStatusRequestOutput() {
    //Given

    //When
    JAXBElement<UpdateVisitStatusRequest> result = outgoingWs.sendUpdateVisitStatusRequestOutput(
        getElement(UpdateVisitStatusRequest.class));

    //Then
    assertNotNull(result);
    assertNull(result.getValue());
  }

  @Test
  public void sendCompleteVisitStatusRequestOutputLowercase() {
    //Given

    //When
    JAXBElement<CompleteVisitRequest> result = outgoingWs.sendCompleteVisitStatusRequestOutputLowercase(
        getElement(CompleteVisitRequest.class));

    //Then
    assertNotNull(result);
    assertNull(result.getValue());
  }

  @Test
  public void sendRequestVisitRequestOutput() {
    //Given

    //When
    JAXBElement<RequestVisitRequest> result = outgoingWs.sendRequestVisitRequestOutput(
        getElement(RequestVisitRequest.class));

    //Then
    assertNotNull(result);
    assertNull(result.getValue());
  }

  @Test
  public void sendSubmitDocumentRequestOutput() {
    //Given

    //When
    JAXBElement<SubmitDocumentRequest> result = outgoingWs.sendSubmitDocumentRequestOutput(
        getElement(SubmitDocumentRequest.class));

    //Then
    assertNotNull(result);
    assertNull(result.getValue());
  }

  @Test
  public void sendCompositeVisitRequestOutput() throws Exception {
    //Given
    ObjectFactory factory = new ObjectFactory();

    CompositeVisitRequest compositeVisitRequest = factory.createCompositeVisitRequest();
    VisitIdentityType visitIdentityType = new VisitIdentityType();
    visitIdentityType.setGuid("testGuid");
    compositeVisitRequest.setIdentity(visitIdentityType);
    JAXBElement<CompositeVisitRequest> compositeVisitRequestJAXBElement = factory
        .createCompositeVisitRequest(compositeVisitRequest);

    //When
    JAXBElement<CompositeVisitRequest> result = outgoingWs
        .sendCompositeVisitRequestOutput(compositeVisitRequestJAXBElement);

    //Then
    verify(rmJobConverterService).transformRequest(compositeVisitRequestJAXBElement);
    assertNotNull(result);
    assertNull(result.getValue());
  }

  @Test
  public void sendSubmitFormResultRequestOutput() {
    //Given

    //When
    JAXBElement<SubmitFormResultRequest> result = outgoingWs.sendSubmitFormResultRequestOutput(
        getElement(SubmitFormResultRequest.class));

    //Then
    assertNotNull(result);
    assertNull(result.getValue());
  }

  @Test
  public void sendSubmitLocationRequestOutput() {
    //Given

    //When
    JAXBElement<SubmitLocationRequest> result = outgoingWs.sendSubmitLocationRequestOutput(
        getElement(SubmitLocationRequest.class));

    //Then
    assertNotNull(result);
    assertNull(result.getValue());
  }
}