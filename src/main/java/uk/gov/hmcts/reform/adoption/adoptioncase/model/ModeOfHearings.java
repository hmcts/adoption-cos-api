package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum ModeOfHearings implements HasLabel {

    @JsonProperty("remotely")
    REMOTELY("Remotely"),

    @JsonProperty("inPerson")
    IN_PERSON("In person");

    private final String label;
}
