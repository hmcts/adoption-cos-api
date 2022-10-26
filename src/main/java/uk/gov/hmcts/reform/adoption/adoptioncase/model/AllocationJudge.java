package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum AllocationJudge implements HasLabel {

    @JsonProperty("allocatePreviousProceedingsJudge")
    ALLOCATE_PREV_PROCEEDINGS_JUDGE("Allocate previous proceedings judge"),

    @JsonProperty("reallocateJudge")
    REALLOCATE_JUDGE("Reallocate judge");

    private final String label;
}
