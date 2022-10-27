package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum ManageHearingOptions implements HasLabel {

    @JsonProperty("addNewHearing")
    ADD_NEW_HEARING("Add a new hearing"),

    @JsonProperty("adjournHearing")
    ADJOURN_HEARING("Adjourn a hearing"),

    @JsonProperty("vacateHearing")
    VACATE_HEARING("Vacate a hearing");

    private final String label;

}
