package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum SiblingRelation implements HasLabel {

    @JsonProperty("sister")
    SISTER("sister"),

    @JsonProperty("halfSister")
    HALF_SISTER("halfSister"),

    @JsonProperty("stepSister")
    STEP_SISTER("stepSister"),

    @JsonProperty("brother")
    BROTHER("brother"),

    @JsonProperty("halfSister")
    HALF_BROTHER("halfBrother"),

    @JsonProperty("stepSister")
    STEP_BROTHER("stepBrother");

    private final String label;

}
