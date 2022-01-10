package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum Nationality implements HasLabel {

    @JsonProperty("British")
    BRITHISH("British"),

    @JsonProperty("Irish")
    IRISH("Irish"),

    @JsonProperty("Other")
    OTHER("Other"),

    @JsonProperty("Not sure")
    NOT_SURE("Not sure");

    private final String label;
}
