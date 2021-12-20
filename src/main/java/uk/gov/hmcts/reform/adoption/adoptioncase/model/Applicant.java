package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.AddressGlobalUK;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.Email;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Builder
public class Applicant {

    @CCD(label = "First name")
    private String firstName;

    @CCD(label = "Middle name(s)")
    private String middleName;

    @CCD(label = "Last name")
    private String lastName;

    @CCD(
        label = "Email address",
        typeOverride = Email
    )
    private String email;

    @CCD(label = "Home address")
    private AddressGlobalUK homeAddress;

    @CCD(
        label = "Phone number",
        regex = "^[0-9 +().-]{9,}$"
    )
    private String phoneNumber;


    @CCD(label = "Keep contact details private?")
    private YesOrNo keepContactDetailsConfidential;

    @CCD(
        label = "Gender",
        hint = "Gender is collected for statistical purposes only.",
        typeOverride = FixedList,
        typeParameterOverride = "Gender"
    )
    private Gender gender;

    @CCD(
        label = "Service address",
        hint = "If they are to be served at their home address, enter the home address here and as the service "
            + "address below"
    )
    private AddressGlobalUK correspondenceAddress;

    @JsonIgnore
    public boolean isConfidentialContactDetails() {
        return null != keepContactDetailsConfidential && keepContactDetailsConfidential.toBoolean();
    }

    @JsonIgnore
    public boolean isBasedOverseas() {
        return !("UK").equalsIgnoreCase(homeAddress.getCountry());
    }
}
