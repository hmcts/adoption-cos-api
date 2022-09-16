package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.AddressUK;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.Email;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Builder
public class OtherAdoptionAgencyOrLocalAuthority {

    @CCD(label = "Name",
        access = {DefaultAccess.class})
    private String otherAdopAgencyOrLaName;

    @CCD(label = "Contact",
        access = {DefaultAccess.class})
    private String otherAdopAgencyOrLaContactName;

    @CCD(access = {DefaultAccess.class},
        label = "Address")
    private AddressUK otherAdopAgencyAddress;

    @CCD(label = "Phone number",
        access = {DefaultAccess.class}
    )
    private String otherAdopAgencyOrLaPhoneNumber;

    @CCD(label = "Email",
        typeOverride = Email,
        access = {DefaultAccess.class}
    )
    private String otherAdopAgencyOrLaContactEmail;

}
