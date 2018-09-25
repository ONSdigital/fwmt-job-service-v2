package uk.gov.ons.fwmt.job_service_v2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

/**
 * Main entry point into the TM Gateway
 *
 * @author Chris Hardman
 */

@Slf4j
@SpringBootApplication
@Configuration
@EnableRetry
public class ApplicationConfig {

  public static void main(String[] args) {
    SpringApplication.run(ApplicationConfig.class, args);
    log.debug("Started application");
  }
}
