package uk.gov.hmcts.reform.adoption.document;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum DocumentTypes implements HasLabel {

    @JsonProperty("application")
    @JsonAlias("adoptionApplication")
    APPLICATION("Application"),

    @JsonProperty("email")
    EMAIL("Email");

    private final String label;
}
