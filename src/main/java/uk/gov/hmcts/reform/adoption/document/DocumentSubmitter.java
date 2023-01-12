package uk.gov.hmcts.reform.adoption.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum DocumentSubmitter implements HasLabel {

    @JsonProperty("childSocialWorker")
    CHILD_SOCIAL_WORKER("Child's local authority"),

    @JsonProperty("adoptionAgencyOrLocalAuthority")
    ADOPTION_AGENCY_OR_LOCAL_AUTHORITY("Adoption agency"),

    @JsonProperty("otherAdoptionAgencyOrLocalAuthority")
    OTHER_ADOPTION_AGENCY_OR_LOCAL_AUTHORITY("Other adoption agency"),

    @JsonProperty("firstApplicant")
    FIRST_APPLICANT("First applicant"),

    @JsonProperty("secondApplicant")
    SECOND_APPLICANT("Second applicant"),

    @JsonProperty("birthMother")
    BIRTH_MOTHER("Respondent (birth mother)"),

    @JsonProperty("birthFather")
    BIRTH_FATHER("Respondent (birth father)"),

    @JsonProperty("personWithParentalResponsibility")
    PERSON_WITH_PARENTAL_RESPONSIBILITY("Other person with parental responsibility"),

    @JsonProperty("legalGuardian")
    LEGAL_GUARDIAN("Legal guardian (Cafcass)"),

    @JsonProperty("applicantLocalAuthority")
    APPLICANT_LOCAL_AUTHORITY("Applicant's local authority"),

    @JsonProperty("otherParty")
    OTHER_PARTY("Other party");

    private final String label;
}
