package uk.gov.ons.fwmt.job_service_v2.utils;

import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.LocationType;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendDeleteJobRequestMessage;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import org.junit.Test;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.Address;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;
import uk.gov.ons.fwmt.job_service_v2.converter.impl.CCSConverter;
import uk.gov.ons.fwmt.job_service_v2.converter.impl.HouseholdConverter;
import uk.gov.ons.fwmt.job_service_v2.converter.impl.OHSConverter;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TMJobConverterTest {

  @Test
  public void createHHJobTest() throws CTPException {
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

    HouseholdConverter converter = new HouseholdConverter("Default", "Mod", 0);
    SendCreateJobRequestMessage request = converter.convert(ingest);

    assertEquals("1234", request.getCreateJobRequest().getJob().getIdentity().getReference());
    assertEquals("188961", request.getCreateJobRequest().getJob().getContact().getName());
    assertEquals(XMLGregorianCalendarImpl.parse("2018-08-16T23:59:59.000Z"),
        request.getCreateJobRequest().getJob().getDueDate());

    assertEquals("\\OPTIMISE\\INPUT", request.getSendMessageRequestInfo().getQueueName());
    assertEquals("1234", request.getSendMessageRequestInfo().getKey());
  }

  @Test
  public void createCCSJobTest() throws CTPException {
    String user = "bob.smith";
    FWMTCreateJobRequest ingest = new FWMTCreateJobRequest();
    Address address = new Address();
    ingest.setActionType("Create");
    ingest.setJobIdentity("1234");
    ingest.setSurveyType("CCS");
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

    CCSConverter converter = new CCSConverter("Default", "Mod", 0);
    SendCreateJobRequestMessage request = converter.convert(ingest);

    assertEquals("1234", request.getCreateJobRequest().getJob().getIdentity().getReference());
    assertEquals("188961", request.getCreateJobRequest().getJob().getContact().getName());
    assertEquals(XMLGregorianCalendarImpl.parse("2018-08-16T23:59:59.000Z"),
        request.getCreateJobRequest().getJob().getDueDate());
    assertEquals("Census - 188961", request.getCreateJobRequest().getJob().getDescription());

    assertEquals("\\OPTIMISE\\INPUT", request.getSendMessageRequestInfo().getQueueName());
    assertEquals("1234", request.getSendMessageRequestInfo().getKey());
  }

  @Test
  public void createLMSJobTest() throws CTPException {
    FWMTCreateJobRequest ingest = new FWMTCreateJobRequest();
    Address address = new Address();
    ingest.setActionType("Create");
    ingest.setJobIdentity("1234");
    ingest.setSurveyType("LMS");
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

    OHSConverter converter = new OHSConverter("Default", "Mod", 0);
    SendCreateJobRequestMessage request = converter.convert(ingest);

    assertEquals("1234", request.getCreateJobRequest().getJob().getIdentity().getReference());
    assertEquals("188961", request.getCreateJobRequest().getJob().getContact().getName());
    assertEquals(XMLGregorianCalendarImpl.parse("2018-08-16T23:59:59.000Z"),
        request.getCreateJobRequest().getJob().getDueDate());
    assertEquals("OHS", request.getCreateJobRequest().getJob().getDescription());
    assertEquals("OHS", request.getCreateJobRequest().getJob().getWorkType());
    assertEquals("Default", request.getCreateJobRequest().getJob().getWorld().getReference());
    assertEquals("test", request.getCreateJobRequest().getJob().getAllocatedTo().getUsername());
    assertEquals("\\OPTIMISE\\INPUT", request.getSendMessageRequestInfo().getQueueName());
    assertEquals("1234", request.getSendMessageRequestInfo().getKey());
    assertEquals(5,
        request.getCreateJobRequest().getJob().getLocation().getAddressDetail().getLines().getAddressLine().size());

  }

  @Test
  public void createLMSJobTestNoAuthNo() throws CTPException {
    FWMTCreateJobRequest ingest = new FWMTCreateJobRequest();
    ingest.setPreallocatedJob(true);
    ingest.setMandatoryResourceAuthNo(null);
    Address address = new Address();
    ingest.setAddress(address);
    ingest.setDueDate(LocalDate.parse("2018-08-16"));
    address.setLatitude(BigDecimal.valueOf(61.7921776));
    address.setLongitude(BigDecimal.valueOf(34.3739957));

    OHSConverter converter = new OHSConverter("Default", "Mod", 0);
    SendCreateJobRequestMessage request = converter.convert(ingest);
    assertEquals("Default", request.getCreateJobRequest().getJob().getWorld().getReference());
    assertNull(request.getCreateJobRequest().getJob().getAllocatedTo());
  }

  @Test
  public void createLMSJobTestNoPreallocatedJob() throws CTPException {
    FWMTCreateJobRequest ingest = new FWMTCreateJobRequest();
    ingest.setPreallocatedJob(false);
    Address address = new Address();
    ingest.setAddress(address);
    ingest.setDueDate(LocalDate.parse("2018-08-16"));
    ingest.setMandatoryResourceAuthNo("1234");
    address.setLatitude(BigDecimal.valueOf(61.7921776));
    address.setLongitude(BigDecimal.valueOf(34.3739957));

    OHSConverter converter = new OHSConverter("Default", "Mod", 0);
    SendCreateJobRequestMessage request = converter.convert(ingest);
    assertEquals("Mod", request.getCreateJobRequest().getJob().getWorld().getReference());
    assertNull(request.getCreateJobRequest().getJob().getMandatoryResource());
  }

  @Test
  public void createLMSJobTestNoPreAllocatedJobNoAuthNo() throws CTPException {
    FWMTCreateJobRequest ingest = new FWMTCreateJobRequest();
    ingest.setPreallocatedJob(false);
    Address address = new Address();
    ingest.setAddress(address);
    ingest.setDueDate(LocalDate.parse("2018-08-16"));
    ingest.setMandatoryResourceAuthNo(null);
    address.setLatitude(BigDecimal.valueOf(61.7921776));
    address.setLongitude(BigDecimal.valueOf(34.3739957));

    OHSConverter converter = new OHSConverter("Default", "Mod", 0);
    SendCreateJobRequestMessage request = converter.convert(ingest);
    assertEquals("Mod", request.getCreateJobRequest().getJob().getWorld().getReference());
    assertNull(request.getCreateJobRequest().getJob().getMandatoryResource());
  }

  @Test
  public void createLMSJobTestNoSurveyType() throws CTPException {
    FWMTCreateJobRequest ingest = new FWMTCreateJobRequest();
    ingest.setPreallocatedJob(true);
    Address address = new Address();
    ingest.setAddress(address);
    ingest.setDueDate(LocalDate.parse("2018-08-16"));
    address.setLatitude(BigDecimal.valueOf(61.7921776));
    address.setLongitude(BigDecimal.valueOf(34.3739957));

    OHSConverter converter = new OHSConverter("Default", "Mod", 0);
    SendCreateJobRequestMessage request = converter.convert(ingest);
    assertEquals("OHS", request.getCreateJobRequest().getJob().getWorkType());

  }

  @Test
  public void addAddressLinesTest() throws DatatypeConfigurationException {
    SendCreateJobRequestMessage message = new CreateJobBuilder(DatatypeFactory.newInstance())
        .addAddressLine("number")
        .addAddressLine("street")
        .addAddressLine("town")
        .addAddressLine("city")
        .build();

    LocationType address = message.getCreateJobRequest().getJob().getLocation();

    assertEquals(4, address.getAddressDetail().getLines().getAddressLine().size());
  }

  @Test
  public void checkNumberOfAddressLinesTest() throws DatatypeConfigurationException {
    SendCreateJobRequestMessage message1 = new CreateJobBuilder(DatatypeFactory.newInstance())
        .addAddressLine("number")
        .addAddressLine("street")
        .addAddressLine("street")
        .addAddressLine("street")
        .addAddressLine("town")
        .addAddressLine("city")
        .build();

    LocationType address1 = message1.getCreateJobRequest().getJob().getLocation();

    assertEquals(6, address1.getAddressDetail().getLines().getAddressLine().size());

    SendCreateJobRequestMessage message2 = new CreateJobBuilder(DatatypeFactory.newInstance())
        .addAddressLine("number")
        .addAddressLine("street")
        .addAddressLine("street")
        .addAddressLine("street")
        .addAddressLine("town")
        .addAddressLine("city")
        .shrinkAddressLines()
        .build();

    LocationType address2 = message2.getCreateJobRequest().getJob().getLocation();

    assertEquals(5, address2.getAddressDetail().getLines().getAddressLine().size());
  }

  @Test
  public void deleteJobTest() {
    SendDeleteJobRequestMessage request = TMJobConverter.deleteJob("1234", "wrong address", "admin");

    assertEquals("wrong address", request.getDeleteJobRequest().getDeletionReason());
    assertEquals("1234", request.getDeleteJobRequest().getIdentity().getReference());
  }
}
