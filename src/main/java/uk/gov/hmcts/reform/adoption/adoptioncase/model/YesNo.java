package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum YesNo implements HasLabel {

    @JsonProperty("Yes")
    YES("Yes"),

    @JsonProperty("No")
    NO("No");

    private final String label;
}
