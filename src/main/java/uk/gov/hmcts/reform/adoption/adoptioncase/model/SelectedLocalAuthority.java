package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum SelectedLocalAuthority implements HasLabel {

    @JsonProperty("fileAdoptionAgencyReport")
    FILE_ADOPTION_AGENCY_REPORT("File adoption agency report");

    private final String label;
}
