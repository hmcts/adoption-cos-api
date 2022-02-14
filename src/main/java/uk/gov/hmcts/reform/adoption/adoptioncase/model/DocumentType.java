package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum DocumentType implements HasLabel {
    @JsonProperty("birthOrAdoptionCertificate")
    BIRTH_OR_ADOPTION_CERTIFICATE("birthOrAdoptionCertificate"),

    @JsonProperty("deathCertificate")
    DEATH_CERTIFICATE("deathCertificate");

    private final String label;
}
