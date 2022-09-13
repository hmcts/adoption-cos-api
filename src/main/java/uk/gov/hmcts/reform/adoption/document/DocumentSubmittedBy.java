package uk.gov.hmcts.reform.adoption.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.FieldType;

@Getter
@AllArgsConstructor
public enum DocumentSubmittedBy {
    @JsonProperty("childSocialWorker")
    @CCD(
        label = "Child's social worker  ${applicant1FirstName}"
    )
    CHILD_SOCIAL_WORKER,

    @JsonProperty("Adoption agency or local authority")
    @CCD(
        label = "Adoption agency or local authority",
        typeOverride = FieldType.Label
    )
    ADOPTION_AGENCY_OR_LOCAL_AUTHORITY,

    @JsonProperty("Other adoption agency or local authority")
    @CCD(
        label = "Other adoption agency or local authority"
    )
    OTHER_ADOPTION_AGENCY_OR_LOCAL_AUTHORITY,

    @JsonProperty("First applicant")
    @CCD(
        label = "First applicant ${applicant1FirstName} ${applicant1LastName}"
    )
    FIRST_APPLICANT,

    @JsonProperty("Second applicant")
    @CCD(
        label = "Second applicant ${applicant2FirstName} ${applicant2LastName}",
        showCondition = "applyingWith!=\"alone\"",
        typeOverride = FieldType.Label
    )
    SECOND_APPLICANT,

    @JsonProperty("Birth mother")
    @CCD(
        label = "Birth mother ${birthMotherFirstName} ${birthMotherLastName}"
    )
    BIRTH_MOTHER,

    @JsonProperty("Birth father")
    @CCD(
        label = "Birth father ${birthFatherFirstName} ${birthFatherLastName}"
    )
    BIRTH_FATHER,

    @JsonProperty("Person with parental responsibility")
    @CCD(
        label = "Person with parental responsibility"
    )
    PERSON_WITH_PARENTAL_RESPONSIBILITY,

    @JsonProperty("Other party")
    @CCD(
        label = "Other party"
    )
    OTHER_PARTY;
}
