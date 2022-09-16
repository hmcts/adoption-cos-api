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

    @CCD(label = "Name",
        access = {DefaultAccess.class}
    )
    private String localAuthority;

    @CCD(label = "Email",
        typeOverride = Email,
        access = {DefaultAccess.class}
    )
    private String localAuthorityEmail;

    @CCD(label = "Address",
        access = {DefaultAccess.class}
    )
    private String socialWorkerAddressLine1;

    @CCD(label = "Town or city",
        access = {DefaultAccess.class}
    )
    private String socialWorkerTown;

    @CCD(label = "Post code",
        access = {DefaultAccess.class}
    )
    private String socialWorkerPostcode;

    @CCD(access = {DefaultAccess.class},
        label = "Address")
    private AddressUK socialWorkerAddress;
}
