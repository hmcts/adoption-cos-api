package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum ManageOrderType implements HasLabel {

    @JsonProperty("caseManagementOrder")
    CASE_MANAGEMENT_ORDER("Case Management Order"),

    @JsonProperty("generalDirectionsOrder")
    GENERAL_DIRECTIONS_ORDER("General Directions Order"),

    @JsonProperty("finalAdoptionOrder")
    FINAL_ADOPTION_ORDER("Final Adoption Order");

    private final String label;

}
