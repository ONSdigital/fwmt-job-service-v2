package uk.gov.ons.fwmt.job_service_v2.transformer;

import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessage;
import org.springframework.integration.annotation.Transformer;
import org.springframework.stereotype.Component;
import uk.gov.ons.fwmt.fwmtgatewaycommon.FWMTCreateJobRequest;

/**
 * Created by soundar on 13/08/2018.
 */
@Component
public class FWMTCreateJobRequestTransformer {

    @Transformer
    public SendCreateJobRequestMessage transform(FWMTCreateJobRequest fwmtCreateJobRequest) {
        return new SendCreateJobRequestMessage();
    }
}
