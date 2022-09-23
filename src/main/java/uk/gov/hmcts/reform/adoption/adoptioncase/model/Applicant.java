package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.AddressUK;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.CollectionAccess;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.Collection;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.Email;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Builder
public class Applicant {

    @CCD(label = "First names")
    private String firstName;

    @CCD(label = "Last names")
    private String lastName;

    @CCD(
        label = "Email address",
        typeOverride = Email
    )
    private String email;

    @CCD(
        label = "Additional Name",
        access = {DefaultAccess.class}
    )
    private YesOrNo hasOtherNames;

    @CCD(
        label = "Previous names",
        typeOverride = Collection,
        typeParameterOverride = "AdditionalName",
        access = {CollectionAccess.class}
    )
    private List<ListValue<AdditionalName>> additionalNames;

    @CCD(
        label = "Date of birth",
        access = {DefaultAccess.class}
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @CCD(label = "Occupation")
    private String occupation;

    @CCD(
        label = "Email address",
        typeOverride = Email
    )
    private String emailAddress;

    @CCD(label = "Phone number")
    private String phoneNumber;

    @CCD(label = "The court may want to use your email to serve you court orders. Are you happy to be served court orders by email?")
    private YesOrNo contactDetailsConsent;

    @CCD(label = "Nationality")
    private SortedSet<Nationality> nationality;

    @CCD(
        label = "Additional Nationalities",
        typeOverride = Collection,
        typeParameterOverride = "OtherNationality",
        access = {CollectionAccess.class}
    )
    private List<ListValue<OtherNationality>> additionalNationalities;

    @CCD(label = "Building and street")
    private String address1;

    @CCD(label = "Address line 2 (optional)")
    private String address2;

    @CCD(label = "Town or city")
    private String addressTown;

    @CCD(label = "County (optional)")
    private String addressCountry;

    @CCD(label = "Postcode")
    private String addressPostCode;

    @CCD(label = "Address same as applicant1")
    private String addressSameAsApplicant1;

    @CCD(label = "contactDetails")
    private Set<ContactDetails> contactDetails;

    @CCD(label = "languagePreference")
    private LanguagePreference languagePreference;

    @CCD(label = "*Enter a UK postcode"
    )
    private AddressUK address;
}
