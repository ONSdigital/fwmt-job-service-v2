package uk.gov.ons.fwmt.job_service_v2.helper;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class TestReceiver {

  public static String result;

  public static int counter;

  public void init() {
    counter = 0;
    result = "";
  }

  private void receiveMessage(String message) {
    counter++;
    result = message;

  }
}
