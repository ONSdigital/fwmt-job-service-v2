package uk.gov.ons.fwmt.job_service_v2.converter;

import com.consiliumtechnologies.schemas.mobile._2009._03.visitstypes.AdditionalPropertyCollectionType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisemessages.CreateJobRequest;
import org.junit.Test;
import uk.gov.ons.fwmt.additionalproperties.AdditionalProperties;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.Address;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;
import uk.gov.ons.fwmt.job_service_v2.converter.impl.LMSConverter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import com.consiliumtechnologies.schemas.mobile._2009._03.visitstypes.AdditionalPropertyType;


public class LMSConverterTest {

  @Test
  public void converter() throws CTPException {

    FWMTCreateJobRequest fwmtCreateJobRequest = new FWMTCreateJobRequest();
    Address address = new Address();
    address.setLine1("test");
    address.setLine2("test");
    address.setLine3("tes");
    address.setLine4("tes");
    address.setTownName("tes");
    address.setPostCode("te1234st");
    Map<String, String> additionalProperties = new HashMap<>();
    additionalProperties.put("caseId", "e225b52c-f8c5-4841-86c7-52f568fb5cd8");
    fwmtCreateJobRequest.setAdditionalProperties(additionalProperties);
    fwmtCreateJobRequest.setActionType("Create");
    fwmtCreateJobRequest.setJobIdentity("1234");
    fwmtCreateJobRequest.setSurveyType("LMS");
    fwmtCreateJobRequest.setPreallocatedJob(true);
    fwmtCreateJobRequest.setMandatoryResourceAuthNo("1234");
    fwmtCreateJobRequest.setDueDate(LocalDate.now());
    fwmtCreateJobRequest.setAddress(address);
    LMSConverter lmsConverter = new LMSConverter();

    CreateJobRequest converted = lmsConverter.convert(fwmtCreateJobRequest);

    assertThat(converted.getJob().getAdditionalProperties().getAdditionalProperty().get(0).getName()).isEqualTo("caseId");
    assertThat(converted.getJob().getAdditionalProperties().getAdditionalProperty().get(0).getValue()).isEqualTo("e225b52c-f8c5-4841-86c7-52f568fb5cd8");
    assertThat(converted.getJob().getAdditionalProperties().getAdditionalProperty().get(1).getName()).isEqualTo("wave");
    assertThat(converted.getJob().getAdditionalProperties().getAdditionalProperty().get(1).getValue()).isEqualTo("1");
    assertThat(converted.getJob().getAdditionalProperties().getAdditionalProperty().get(2).getName()).isEqualTo("TLA");
    assertThat(converted.getJob().getAdditionalProperties().getAdditionalProperty().get(2).getValue()).isEqualTo("OHS");
  }

}
