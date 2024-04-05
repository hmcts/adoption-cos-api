package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum FurtherInformation implements HasLabel {

    @JsonProperty("askForAdditionalDocument")
    ASK_FOR_ADDITIONAL_DOCUMENT("Ask for additional documents"),

    @JsonProperty("askAQuestion")
    ASK_A_QUESTION("Ask a question");

    private final String label;
}
