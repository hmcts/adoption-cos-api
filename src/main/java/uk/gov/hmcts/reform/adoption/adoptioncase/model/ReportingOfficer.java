package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum ReportingOfficer implements HasLabel {

    @JsonProperty("cafcass")
    REMOTELY("Cafcass"),

    @JsonProperty("cafcassCymru")
    IN_PERSON("Cafcass Cymru");

    private final String label;
}
