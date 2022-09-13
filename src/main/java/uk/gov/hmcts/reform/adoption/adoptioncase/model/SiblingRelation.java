package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum SiblingRelation implements HasLabel {

    @JsonProperty("Sister")
    SISTER("Sister"),

    @JsonProperty("Half-Sister")
    HALF_SISTER("Half-sister"),

    @JsonProperty("Step-Sister")
    STEP_SISTER("Step-sister"),

    @JsonProperty("Brother")
    BROTHER("Brother"),

    @JsonProperty("Half-brother")
    HALF_BROTHER("Half-brother"),

    @JsonProperty("Step-brother")
    STEP_BROTHER("Step-brother");

    private final String label;

}
