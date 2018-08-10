package uk.gov.ons.fwmt.job_service_v2.service.tm;

public interface TMService {
  <I, O> O send(I message);
}
