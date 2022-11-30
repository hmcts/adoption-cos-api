package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.DynamicRadioList;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedRadioList;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.TextArea;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.MultiSelectList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdoptionOrderData {

    @CCD(
        access = {DefaultAccess.class}
    )
    private Document draftDocument;

    @CCD(showCondition = "submittedDateAdoptionOrder=\"never\"")
    private String orderId;

    @CCD(access = {DefaultAccess.class})
    private LocalDateTime submittedDateAdoptionOrder;

    @CCD(showCondition = "submittedDateAdoptionOrder=\"never\"")
    private OrderStatus orderStatus;

    @CCD(hint = "Copy and paste or type directly into the text box",
        typeOverride = TextArea,
        access = {DefaultAccess.class})
    private String preambleDetailsFinalAdoptionOrder;

    @CCD(hint = "Enter the name of the judge issuing this order",
        access = {DefaultAccess.class})
    private String orderedByFinalAdoptionOrder;

    @CCD(
        access = {DefaultAccess.class}
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOrderMadeFinalAdoptionOrder;

    @CCD(access = {DefaultAccess.class})
    private String costOrdersFinalAdoptionOrder;

    @CCD(
        typeOverride = DynamicRadioList,
        hint = "Agency or local authority which placed the child for adoption"
    )
    private DynamicList placementOfTheChildList;

    @CCD(
        typeOverride = MultiSelectList,
        hint = "Recipients of Final adoption order (A76)",
        typeParameterOverride = "RecipientsA76"
    )
    private Set<RecipientsA76> recipientsListA76;

    @CCD(
        typeOverride = MultiSelectList,
        hint = "Recipients of Final adoption order (A206)",
        typeParameterOverride = "RecipientsA206"
    )
    private Set<RecipientsA206> recipientsListA206;

    @CCD(
        access = {DefaultAccess.class}
    )
    private Document draftDocument;

    @Getter
    @AllArgsConstructor
    public enum RecipientsA76 implements HasLabel {

        @JsonProperty("firstApplicant")
        FIRST_APPLICANT("First applicant"),

        @JsonProperty("secondApplicant")
        SECOND_APPLICANT("Second applicant");

        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum RecipientsA206 implements HasLabel {

        @JsonProperty("respondentBirthMother")
        RESPONDENT_BIRTH_MOTHER("Respondent (Birth mother)"),

        @JsonProperty("respondentBirthFather")
        RESPONDENT_BIRTH_FATHER("Respondent (Birth father)"),

        @JsonProperty("legalGuardianCAFCASS")
        LEGAL_GUARDIAN_CAFCASS("Legal guardian (CAFCASS)"),

        @JsonProperty("childsLocalAuthority")
        CHILDS_LOCAL_AUTHORITY("Child's local authority"),

        @JsonProperty("applicantsLocalAuthority")
        APPLICANTS_LOCAL_AUTHORITY("Applicant's local authority"),

        @JsonProperty("adoptionAgency")
        ADOPTION_AGENCY("Adoption agency"),

        @JsonProperty("otherAdoptionAgency")
        OTHER_ADOPTION_AGENCY("Other adoption agency"),

        @JsonProperty("otherPersonWithParentalResponsibility")
        OTHER_PARENT_WITH_PARENT_RESPONSIBILITY("Other person with parental responsibility");

        private final String label;
    }

    @CCD(
        access = {DefaultAccess.class}
    )
    private YesOrNo placeOfBirthProved;

    @CCD(
        typeOverride = FixedRadioList,
        typeParameterOverride = "TypeOfCertificate",
        access = {DefaultAccess.class}
    )
    private TypeOfCertificate typeOfCertificate;

    @CCD(
        typeOverride = FixedRadioList,
        typeParameterOverride = "CountryOfBirth",
        access = {DefaultAccess.class}
    )
    private CountryOfBirth countryOfBirthForPlaceOfBirthYes;

    @CCD(
        typeOverride = FixedRadioList,
        typeParameterOverride = "CountryOfBirth",
        access = {DefaultAccess.class}
    )
    private CountryOfBirth countryOfBirthForPlaceOfBirthNo;

    @CCD
    private String otherCountryOfOriginForPlaceOfBirthYes;

    @CCD
    private String otherCountryOfOriginForPlaceOfBirthNo;

    @CCD(
        access = {DefaultAccess.class}
    )
    private YesOrNo timeOfBirthKnown;

    @CCD(
        hint = "Add time of birth in a 24 hour format such as 14:01",
        access = {DefaultAccess.class}
    )
    private String timeOfBirth;

    @CCD(
        access = {DefaultAccess.class}
    )
    private String birthAdoptionRegistrationNumber;

    @CCD(
        access = {DefaultAccess.class}
    )
    private String registrationDistrict;

    @CCD(
        access = {DefaultAccess.class}
    )
    private String registrationSubDistrict;

    @CCD(
        access = {DefaultAccess.class}
    )
    private String registrationCounty;

    @CCD(
        access = {DefaultAccess.class}
    )
    private LocalDate adoptionRegistrationDate;

}
