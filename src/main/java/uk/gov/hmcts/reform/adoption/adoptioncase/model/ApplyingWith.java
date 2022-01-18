package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum ApplyingWith implements HasLabel {

    @JsonProperty("alone")
    ALONE("I'm applying on my own"),

    @JsonProperty("withSpouseOrCivilPartner")
    WITH_SPOUSE_OR_CIVIL_PARTNER("I'm applying with my spouse or civil partner"),

    @JsonProperty("withSomeoneElse")
    WITH_SOME_ONE_ELSE("I'm applying with someone who is not my spouse or civil partner");

    private final String label;
}
