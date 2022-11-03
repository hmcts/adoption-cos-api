package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum Cafcass implements HasLabel {

    @JsonProperty("reportingOfficer")
    REPORTING_OFFICER("Reporting officer"),

    @JsonProperty("childrensGuardian")
    CHILDRENS_GUARDIAN("Children's guardian");

    private final String label;
}
