package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum MessageUrgency implements HasLabel {

    @JsonProperty("high")
    HIGH("High"),

    @JsonProperty("medium")
    MEDIUM("Medium"),

    @JsonProperty("low")
    LOW("Low");

    private final String label;
}

