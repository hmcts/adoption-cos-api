package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum MethodOfHearing implements HasLabel {

    @JsonProperty("remote")
    REMOTE("Remote (via video hearing)"),

    @JsonProperty("inPerson")
    IN_PERSON("In person");

    private String label;
}
