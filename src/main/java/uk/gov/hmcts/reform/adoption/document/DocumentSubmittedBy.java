package uk.gov.hmcts.reform.adoption.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum DocumentSubmittedBy implements HasLabel {
    @JsonProperty("childSocialWorker")
    CHILD_SOCIAL_WORKER("Child's social worker"),

    @JsonProperty("adoptionAgencyOrLocalAuthority")
    ADOPTION_AGENCY_OR_LOCAL_AUTHORITY("Adoption agency or local authority"),

    @JsonProperty("otherAdoptionAgencyOrLocalAuthority")
    OTHER_ADOPTION_AGENCY_OR_LOCAL_AUTHORITY("Other adoption agency or local authority"),

    @JsonProperty("firstApplicant")
    FIRST_APPLICANT("First applicant"),

    @JsonProperty("secondApplicant")
    SECOND_APPLICANT("Second applicant"),

    @JsonProperty("birthMother")
    BIRTH_MOTHER("Birth mother"),

    @JsonProperty("birthFather")
    BIRTH_FATHER("Birth father"),

    @JsonProperty("personWithParentalResponsibility")
    PERSON_WITH_PARENTAL_RESPONSIBILITY("Person with parental responsibility")/*,

    @JsonProperty("otherParty")
    OTHER_PARTY("Other party")*/;

    private final String label;
}
