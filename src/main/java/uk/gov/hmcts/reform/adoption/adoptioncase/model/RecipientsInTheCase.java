package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum RecipientsInTheCase implements HasLabel {

    @JsonProperty("firstApplicant")
    APPLICANT1("First applicant"),

    @JsonProperty("secondApplicant")
    APPLICANT2("Second applicant"),

    @JsonProperty("respondentBirthMother")
    RESPONDENT_BIRTH_MOTHER("Respondent (birth mother)"),

    @JsonProperty("respondentBirthFather")
    RESPONDENT_BIRTH_FATHER("Respondent (birth father)"),

    @JsonProperty("legalGuardianCafcass")
    LEGAL_GUARDIAN_CAFCASS("Legal guardian (Cafcass)"),

    @JsonProperty("childsLocalAuthority")
    CHILDS_LOCAL_AUTHORITY("Child's local authority"),

    @JsonProperty("applicantsLocalAuthority")
    APPLICANTS_LOCAL_AUTHORITY("Applicant's local authority"),

    @JsonProperty("adoptionAgency")
    ADOPTION_AGENCY("Adoption agency"),

    @JsonProperty("otherAdoptionAgency")
    OTHER_ADOPTION_AGENCY("Other adoption agency"),

    @JsonProperty("otherPersonWithParentalResponsibility")
    OTHER_PERSON_WITH_PARENTAL_RESPONSIBILITY("Other person with parental responsibility");

    private final String label;
}
