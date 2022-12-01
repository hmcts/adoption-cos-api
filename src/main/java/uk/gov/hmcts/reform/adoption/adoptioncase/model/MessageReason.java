package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum MessageReason implements HasLabel {

    @JsonProperty("referToGatekeeper")
    REFER_TO_GATEKEEPER("Refer to gatekeeper"),

    @JsonProperty("referToJudge")
    REFER_TO_JUDGE("Refer to judge"),

    @JsonProperty("requestReviewDocuments")
    REQUEST_REVIEW_DOCUMENTS("Request review of documents");

    private final String label;
}
