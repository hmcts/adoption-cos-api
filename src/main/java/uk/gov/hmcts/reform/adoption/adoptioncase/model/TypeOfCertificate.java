package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum TypeOfCertificate implements HasLabel {

    @JsonProperty("birthCertificate")
    BIRTH_CERTIFICATE("Birth certificate"),

    @JsonProperty("adoptionCertificate")
    ADOPTION_CERTIFICATE("Adoption certificate");

    private final String label;

}
