package uk.gov.hmcts.reform.adoption.payment;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.gov.hmcts.reform.adoption.payment.model.CreditAccountPaymentRequest;
import uk.gov.hmcts.reform.adoption.payment.model.CreditAccountPaymentResponse;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static uk.gov.hmcts.reform.adoption.common.config.ControllerConstants.SERVICE_AUTHORIZATION;

@FeignClient(name = "fees-and-payments-client", url = "${payment.service.api.baseurl}")
@SuppressWarnings("PMD.UseObjectForClearerAPI")
public interface PaymentPbaClient {

    @ApiOperation("Handles Adoption Payment By Account (PBA) Payments")
    @PostMapping(value = "/credit-account-payments")
    ResponseEntity<CreditAccountPaymentResponse> creditAccountPayment(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SERVICE_AUTHORIZATION) String serviceAuthorisation,
        CreditAccountPaymentRequest creditAccountPaymentRequest);

}
