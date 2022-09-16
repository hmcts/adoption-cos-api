package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.Email;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Builder
public class Guardian {


    @CCD(label = "Full name of contact",
        access = {DefaultAccess.class})
    private String name;

    @CCD(
        label = "Email",
        typeOverride = Email
    )
    private String email;

    @CCD(label = "Phone number")
    private String phoneNumber;

    @CCD(label = "Address line 1")
    private String address1;

    @CCD(label = "Address line 2")
    private String address2;

    @CCD(label = "Address line 3")
    private String address3;

    @CCD(label = "Town or city")
    private String addressTown;

    @CCD(label = "County, district, state or province")
    private String addressCounty;

    @CCD(label = "Postcode")
    private String addressPostCode;

    @CCD(label = "Country")
    private String country;

}
