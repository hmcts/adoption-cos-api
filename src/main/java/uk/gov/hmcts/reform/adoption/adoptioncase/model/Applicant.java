package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.Email;
//import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedList;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.PhoneUK;
//import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Builder
public class Applicant {

    @CCD(label = "First name")
    private String firstName;

    /*@CCD(label = "Middle name(s)")
    private String middleName;*/

    @CCD(label = "Last name")
    private String lastName;

    @CCD(
        label = "Email address",
        typeOverride = Email
    )
    private String email;

    @CCD(label = "Applicant full name")
    private String fullName;

    @CCD(
        label = "Additional Name",
        access = {DefaultAccess.class}
    )
    private YesOrNo hasOtherNames;

    @CCD(
        label = "Additional Names",
        //typeOverride = Collection,
        //typeParameterOverride = "String",
        access = {DefaultAccess.class}
    )
    private List<OtherName> additionalNames;

    @CCD(
        label = "Date of Birth",
        access = {DefaultAccess.class}
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @CCD(label = "Applicant Occupation")
    private String occupation;

    @CCD(
        label = "Email address",
        typeOverride = Email
    )
    private String emailAddress;

    @CCD(label = "Applicant phoneNumber",
        typeOverride = PhoneUK
    )
    private String phoneNumber;

    @CCD(label = "Nationality")
    private Set<Nationality> nationality;

    @CCD(label = "Address1")
    private String address1;

    @CCD(label = "Address2")
    private String address2;

    @CCD(label = "Town")
    private String town;

    @CCD(label = "Country")
    private String country;

    @CCD(label = "Post code")
    private String postCode;

    /*@CCD(label = "Home address")
    private AddressGlobalUK homeAddress;*/

    /*@CCD(
        label = "Service address",
        hint = "If they are to be served at their home address, enter the home address here and as the service "
            + "address below"
    )
    private AddressGlobalUK correspondenceAddress;*/
}
