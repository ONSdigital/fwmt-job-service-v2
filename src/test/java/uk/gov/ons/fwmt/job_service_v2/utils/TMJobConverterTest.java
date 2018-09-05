package uk.gov.ons.fwmt.job_service_v2.utils;

import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendDeleteJobRequestMessage;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import org.junit.Before;
import org.junit.Test;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.Address;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;

import javax.xml.datatype.DatatypeConfigurationException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TMJobConverterTest {

  TMJobConverter tmJobConverter;

  @Before
  public void setup() throws DatatypeConfigurationException {
    tmJobConverter = new TMJobConverter();
  }

  @Test
  public void createJobTest() throws DatatypeConfigurationException {
    String user = "bob.smith";
    FWMTCreateJobRequest ingest = new FWMTCreateJobRequest();
    Address address = new Address();
    ingest.setActionType("Create");
    ingest.setJobIdentity("1234");
    ingest.setSurveyType("HH");
    ingest.setPreallocatedJob(true);
    ingest.setMandatoryResourceAuthNo("1234");
    ingest.setDueDate(LocalDate.parse("2018-08-16"));
    address.setLine1("886");
    address.setLine2("Prairie Rose");
    address.setLine3("Trail");
    address.setLine4("RU");
    address.setTownName("Borodinskiy");
    address.setPostCode("188961");
    address.setLatitude(BigDecimal.valueOf(61.7921776));
    address.setLongitude(BigDecimal.valueOf(34.3739957));
    ingest.setAddress(address);

    SendCreateJobRequestMessage request = tmJobConverter.createJob(ingest, user);

    assertEquals(request.getCreateJobRequest().getJob().getIdentity().getReference(), "1234");
    assertEquals(request.getCreateJobRequest().getJob().getContact().getName(), "188961");
    assertEquals(request.getCreateJobRequest().getJob().getDueDate(),
        XMLGregorianCalendarImpl.parse("2018-08-16T23:59:59.000Z"));

    assertEquals(request.getSendMessageRequestInfo().getQueueName(), "\\OPTIMISE\\INPUT");
    assertEquals(request.getSendMessageRequestInfo().getKey(), "1234");
  }

  @Test
  public void addAddressLinesTest() {
    List<String> addressLines = new ArrayList<>();
    String addressLine1 = "number";
    String addressLine2 = "street";
    String addressLine3 = "town";
    String addressLine4 = "city";

    TMJobConverter.addAddressLines(addressLines, addressLine1);
    TMJobConverter.addAddressLines(addressLines, addressLine2);
    TMJobConverter.addAddressLines(addressLines, addressLine3);
    TMJobConverter.addAddressLines(addressLines, addressLine4);

    assertEquals(4, addressLines.size());
  }

  @Test
  public void checkNumberOfAddressLinesTest() {
    List<String> addressLines = new ArrayList<>();
    String addressLine1 = "number";
    String addressLine2 = "street";
    String addressLine3 = "street";
    String addressLine4 = "street";
    String addressLine5 = "town";
    String addressLine6 = "city";

    TMJobConverter.addAddressLines(addressLines, addressLine1);
    TMJobConverter.addAddressLines(addressLines, addressLine2);
    TMJobConverter.addAddressLines(addressLines, addressLine3);
    TMJobConverter.addAddressLines(addressLines, addressLine4);
    TMJobConverter.addAddressLines(addressLines, addressLine5);
    TMJobConverter.addAddressLines(addressLines, addressLine6);

    assertEquals(6, addressLines.size());

    TMJobConverter.checkNumberOfAddressLines(addressLines);

    assertEquals(5, addressLines.size());
  }

  @Test
  public void deleteJobTest() {
    SendDeleteJobRequestMessage request = tmJobConverter.deleteJob("1234", "wrong address","admin");

    assertEquals(request.getDeleteJobRequest().getDeletionReason(), "wrong address");
    assertEquals(request.getDeleteJobRequest().getIdentity().getReference(), "1234");
  }
}
