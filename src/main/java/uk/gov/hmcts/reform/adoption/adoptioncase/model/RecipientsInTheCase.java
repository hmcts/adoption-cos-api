package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum RecipientsInTheCase implements HasLabel {

    @CCD(showCondition = "recipientsInTheCase-respondentBirthMother=\"No\"")
    @JsonProperty("respondentBirthMother")
    RESPONDENT_MOTHER("Respondent(birth mother)"),

    @CCD(showCondition = "birthFatheroBeServed=\"No\"")
    @JsonProperty("respondentBirthFather")
    RESPONDENT_FATHER("Respondent(birth father)"),

    /*@JsonProperty("applicant")
    APPLICANT("Applicant"),*/

    @JsonProperty("applicant1")
    APPLICANT1("Applicant 1"),

    @JsonProperty("applicant2")
    APPLICANT2("Applicant 2"),

    @CCD(showCondition = "isChildRepresentedByGuardian=\"Yes\"")
    @JsonProperty("legalGuardian")
    LEGAL_GUARDIAN("Legal guardian (CAFCASS)"),

    @JsonProperty("childSolicitor")
    CHILD_SOLICITOR("Child solicitor"),

    @JsonProperty("applicantSolicitor")
    APPLICANT_SOLICITOR("Applicant solicitor"),

    @JsonProperty("adoptionAgency")
    ADOPTION_AGENCY("Adoption agency"),

    @JsonProperty("otherAdoptionAgency")
    OTHER_ADOPTION_AGENCY("Other adoption agency"),

    @JsonProperty("childLocalAuthority")
    CHILD_LOCAL_AUTHORITY("Child local authority"),

    @JsonProperty("applicantLocalAuthority")
    APPLICANT_LOCAL_AUTHORITY("Applicant local authority"),

    @JsonProperty("birthMotherSolicitor")
    BIRTH_MOTHER_SOLICITOR("Birth mother solicitor"),

    @JsonProperty("birthFatherSolicitor")
    BIRTH_FATHER_SOLICITOR("Birth father solicitor"),

    @JsonProperty("otherPersonWithParentalResponsibility")
    OTHER_PERSON_WITH_PARENTAL_RESPONSIBILITIES("Other person with parental responsibility"),

    @JsonProperty("otherPersonWithParentalResponsibilitySolicitor")
    OTHER_PERSON_WITH_PARENTAL_RESPONSIBILITIES_SOLICITOR("Other person with parental responsibility solicitor");

    private String label;
}
