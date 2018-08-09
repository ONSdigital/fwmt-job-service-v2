package uk.gov.ons.fwmt.job_service_v2.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobDto {

    private String tmJobId;

    private String lastAuthNo;

}