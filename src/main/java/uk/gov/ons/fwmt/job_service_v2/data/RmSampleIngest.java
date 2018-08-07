package uk.gov.ons.fwmt.job_service_v2.data;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RmSampleIngest {

    public String jobIdentity;
    public String addressLine1;
    public String addressLine2;
    public String addressLine3;
    public String addressLine4;
    public String townName;
    public String postcode;
    public String latitude;
    public String surveyType;
    public LocalDate dueDate;
    public boolean preallocatedJob;
    public String mandatoryResourceAuthNo;

}
