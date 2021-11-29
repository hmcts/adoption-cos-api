package uk.gov.hmcts.reform.adoption.solicitor.client.pba;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static uk.gov.hmcts.reform.adoption.common.config.ControllerConstants.SERVICE_AUTHORIZATION;


@FeignClient(name = "pba-ref-data-client", url = "${pba.ref.data.service..url}")
public interface PbaRefDataClient {

    @ApiOperation("Retrieves Solicitor Pay By Account (PBA) numbers for payment")
    @GetMapping(value = "/refdata/external/v1/organisations/pbas")
    ResponseEntity<PbaOrganisationResponse> retrievePbaNumbers(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SERVICE_AUTHORIZATION) String serviceAuthorisation,
        @RequestHeader("UserEmail") String email);
}
