package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.SystemUpdateAccess;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.DynamicRadioList;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedRadioList;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.TextArea;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdoptionOrderData {

    @CCD(access = {DefaultAccess.class})
    private LocalDateTime submittedDateAdoptionOrder;

    @CCD(hint = "Copy and paste or type directly into the text box",
        typeOverride = TextArea,
        access = {DefaultAccess.class})
    private String preambleDetailsFinalAdoptionOrder;

    @CCD(hint = "Enter the name of the judge issuing this order",
        access = {DefaultAccess.class})
    private String orderedByFinalAdoptionOrder;

    @CCD(access = {DefaultAccess.class})
    private String costOrdersFinalAdoptionOrder;

    @CCD(
        typeOverride = DynamicRadioList,
        hint = "Agency or local authority which placed the child for adoption"
    )
    private DynamicList placementOfTheChildList;

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
