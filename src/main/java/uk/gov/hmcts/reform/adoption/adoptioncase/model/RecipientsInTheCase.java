package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum RecipientsInTheCase implements HasLabel {

    @JsonProperty("applicant1")
    APPLICANT1("First applicant"),

    @JsonProperty("applicant2")
    APPLICANT2("Second applicant"),

    @JsonProperty("respondentBirthMother")
    RESPONDENT_MOTHER("Respondent (birth mother)"),

    @JsonProperty("respondentBirthFather")
    RESPONDENT_FATHER("Respondent (birth father)"),

    @JsonProperty("legalGuardian")
    LEGAL_GUARDIAN("Legal guardian (Cafcass)"),

    @JsonProperty("childLocalAuthority")
    CHILD_LOCAL_AUTHORITY("Child's local authority"),

    @JsonProperty("applicantLocalAuthority")
    APPLICANT_LOCAL_AUTHORITY("Applicant's local authority"),

    @JsonProperty("adoptionAgency")
    ADOPTION_AGENCY("Adoption agency"),

    @JsonProperty("otherAdoptionAgency")
    OTHER_ADOPTION_AGENCY("Other adoption agency"),

    @JsonProperty("otherPersonWithParentalResponsibility")
    OTHER_PERSON_WITH_PARENTAL_RESPONSIBILITIES("Other person with parental responsibility");

    private final String label;
}
