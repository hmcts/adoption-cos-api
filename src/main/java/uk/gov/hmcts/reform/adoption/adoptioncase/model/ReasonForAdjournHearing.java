package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum ReasonForAdjournHearing implements HasLabel {


    @JsonProperty("specialMeasuresRequired")
    SPECIAL_MEASURES_REQUIRED("Special measures required"),

    @JsonProperty("interpreterRequired")
    COURT_OR_JUDGE_UNAVAILABLE("Interpreter required"),

    @JsonProperty("insufficientTimeToDealWithCases")
    INSUFFICIENT_TIME_TO_DEALS_WITH_CASES("Insufficient time to deal with cases"),

    @JsonProperty("adminError")
    ADMIN_ERROR("Admin error"),

    @JsonProperty("notReadyToProceed")
    NOT_READY_TO_PROCEED("Not ready to proceed"),

    @JsonProperty("cafcass")
    CAFCASS("Cafcass"),

    @JsonProperty("experts")
    EXPERTS("Experts"),

    @JsonProperty("health")
    HEALTH("Health"),

    @JsonProperty("international")
    INTERNATIONAL("International"),

    @JsonProperty("localAuthority")
    LOCAL_AUTHORITY("Local authority"),

    @JsonProperty("otherFreeTextBox")
    OTHER_FREETEXT_BOX("Other");

    private final String label;
}
