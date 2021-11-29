package uk.gov.hmcts.reform.adoption.divorcecase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum ServicePaymentMethod implements HasLabel {

    @JsonProperty("feePayByAccount")
    FEE_PAY_BY_ACCOUNT("Fee account"),

    @JsonProperty("feePayByHelp")
    FEE_PAY_BY_HWF("Help with Fees"),

    @JsonProperty("feePayByTelephone")
    FEE_PAY_BY_PHONE("Telephone"),

    @JsonProperty("feePayByCheque")
    FEE_PAY_BY_CHEQUE("Cheque");

    private final String label;
}
