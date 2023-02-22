package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum ResponsibilityReasons implements HasLabel {

    @JsonProperty("birthCertificate")
    BIRTH_CERTIFICATE("Birth certificate"),

    @JsonProperty("courtOrder")
    COURT_ORDER("Court order"),

    @JsonProperty("parentalResponsibilityOrder")
    PARENTAL_RESPONSIBILITY_ORDER("Parental responsibility order"),

    @JsonProperty("parentalResponsibilityAgreement")
    PARENTAL_RESPONSIBILITY_AGREEMENT("Parental responsibility agreement"),

    @JsonProperty("parentalResponsibilityRemovedByCourt")
    PARENTAL_RESPONSIBILITY_REMOVED_BY_COURT("Parental responsibility removed by court"),

    @JsonProperty("parentalResponsibilityNeverObtained")
    PARENTAL_RESPONSIBILITY_NEVER_OBTAINED("Parental responsibility never obtained"),

    @JsonProperty("other")
    OTHER("Other");

    private final String label;
}
