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
public class AdoptionAgencyOrLocalAuthority {

    @CCD(label = "Name",
        access = {DefaultAccess.class})
    private String adopAgencyOrLaName;

    @CCD(label = "Contact",
        access = {DefaultAccess.class})
    private String adopAgencyOrLaContactName;

    @CCD(label = "Address line 1")
    private String adopAgencyAddressLine1;

    @CCD(label = "Address line 2")
    private String adopAgencyAddressLine2;

    @CCD(label = "Address line 3")
    private String adopAgencyAddressLine3;

    @CCD(label = "Town or city",
        access = {DefaultAccess.class}
    )
    private String adopAgencyTown;

    @CCD(label = "County, district, state or province")
    private String adopAgencyAddressCounty;

    @CCD(label = "Post code",
        access = {DefaultAccess.class}
    )
    private String adopAgencyPostcode;

    @CCD(label = "Country")
    private String adopAgencyCountry;

    @CCD(label = "Phone number",
        access = {DefaultAccess.class}
    )
    private String adopAgencyOrLaPhoneNumber;

    @CCD(label = "Email",
        typeOverride = Email
    )
    private String adopAgencyOrLaContactEmail;

}
