package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum ResponsibilityReasons implements HasLabel {

    @JsonProperty("Birth certificate")
    BIRTH_CERTIFICATE("Birth certificate"),

    @JsonProperty("Court order")
    COURT_ORDER("Court order"),

    @JsonProperty("Parental responsibility order")
    PARENTAL_RESPONSIBILITY_ORDER("Parental responsibility order"),

    @JsonProperty("Parental responsibility agreement")
    PARENTAL_RESPONSIBILITY_AGREEMENT("Parental responsibility agreement"),

    @JsonProperty("Parental responsibility removed by court")
    PARENTAL_RESPONSIBILITY_REMOVED_BY_COURT("Parental responsibility removed by court"),

    @JsonProperty("Parental responsibility never obtained")
    PARENTAL_RESPONSIBILITY_NEVER_OBTAINED("Parental responsibility never obtained"),

    @JsonProperty("Other")
    OTHER("Other");

    private final String label;
}
