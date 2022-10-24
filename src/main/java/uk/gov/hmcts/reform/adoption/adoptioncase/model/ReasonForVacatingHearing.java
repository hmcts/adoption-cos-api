package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum ReasonForVacatingHearing implements HasLabel {

    @JsonProperty("agreementConsentOrderMade")
    AGREEMENT_OR_CONSENT_ORDER_MADE("Agreement or consent order made"),

    @JsonProperty("caseWithdrawn")
    CASE_WITHDRAWN("Case withdraw"),

    @JsonProperty("caseDismissed")
    CASE_DISMISSED("Case dismissed");


    private final String label;
}
