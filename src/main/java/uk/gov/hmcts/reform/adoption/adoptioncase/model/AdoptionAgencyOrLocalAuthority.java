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
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@Builder
public class AdoptionAgencyOrLocalAuthority {

    @CCD(label = "Name of Agency",
        access = {DefaultAccess.class})
    private String adopAgencyOrLaName;

    @CCD(label = "Name of Contact",
        access = {DefaultAccess.class})
    private String adopAgencyOrLaContactName;

    @CCD(label = "Address",
        access = {DefaultAccess.class}
    )
    private String adopAgencyAddressLine1;

    @CCD(label = "Town or city",
        access = {DefaultAccess.class}
    )
    private String adopAgencyTown;

    @CCD(label = "Post code",
        access = {DefaultAccess.class}
    )
    private String adopAgencyPostcode;

    @CCD(label = "Phone number",
        access = {DefaultAccess.class}
    )
    private String adopAgencyOrLaPhoneNumber;

    @CCD(label = "Email",
        typeOverride = Email,
        access = {DefaultAccess.class}
    )
    private String adopAgencyOrLaContactEmail;

    // Bulk Scan phase 2 starts

    @CCD(label = "Child placed for the purpose of Adoption",
        access = {DefaultAccess.class}
    )
    private String laOrAdoptionAgencyCategory;

    // Bulk Scan phase 2 ends

}
