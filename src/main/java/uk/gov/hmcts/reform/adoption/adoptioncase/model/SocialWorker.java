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
public class SocialWorker {

    @CCD(label = "Name ",
        access = {DefaultAccess.class})
    private String socialWorkerName;

    @CCD(label = "Phone number",
        access = {DefaultAccess.class}
    )
    private String socialWorkerPhoneNumber;

    @CCD(label = "Social Worker Email",
        typeOverride = Email,
        access = {DefaultAccess.class})
    private String socialWorkerEmail;

    @CCD(label = "Contact",
        access = {DefaultAccess.class}
    )
    private String localAuthority;

    @CCD(label = "Email",
        typeOverride = Email,
        access = {DefaultAccess.class}
    )
    private String localAuthorityEmail;

    @CCD(label = "Address line 1",
        access = {DefaultAccess.class}
    )
    private String socialWorkerAddressLine1;

    @CCD(label = "Address line 2",
        access = {DefaultAccess.class})
    private String socialWorkerAddressLine2;

    @CCD(label = "Address line 3",
        access = {DefaultAccess.class})
    private String socialWorkerAddressLine3;

    @CCD(label = "Town or city",
        access = {DefaultAccess.class}
    )
    private String socialWorkerTown;

    @CCD(label = "County, district, state or province")
    private String socialWorkerAddressCounty;

    @CCD(label = "Post code",
        access = {DefaultAccess.class}
    )
    private String socialWorkerPostcode;

    @CCD(label = "Country",
        access = {DefaultAccess.class})
    private String socialWorkerCountry;
}
