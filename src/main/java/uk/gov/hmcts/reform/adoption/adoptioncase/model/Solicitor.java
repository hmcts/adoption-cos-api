package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.AddressUK;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.Email;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.PhoneUK;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Builder
public class Solicitor {

    @CCD(label = "Solicitor",
        access = {DefaultAccess.class})
    private String solicitorFirm;

    @CCD(label = "Reference Number",
        access = {DefaultAccess.class})

    private String solicitorRef;

    @CCD(
        label = "Email",
        typeOverride = Email,
        access = {DefaultAccess.class}
    )
    private String email;

    @CCD(label = "Phone number",
        access = {DefaultAccess.class},
        typeOverride = PhoneUK)
    private String phoneNumber;

    @CCD(label = "Address",
        access = {DefaultAccess.class})
    private AddressUK solicitorAddress;

    @CCD(
        label = "Solicitor Helping With Application",
        access = {DefaultAccess.class}
    )
    private YesOrNo solicitorHelpingWithApplication;

}
