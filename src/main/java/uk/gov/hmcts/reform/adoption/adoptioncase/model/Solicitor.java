package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.Email;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@Builder
public class Solicitor {

    @CCD(label = "Solicitor’s Firm",
        access = {DefaultAccess.class})
    private String solicitorFirm;

    @CCD(label = "Solicitor’s Name",
        access = {DefaultAccess.class})
    private String solicitorName;

    @CCD(
        label = "Solicitor’s Phone number",
        access = {DefaultAccess.class}
    )
    private String solicitorPhoneNumber;

    @CCD(
        label = "Solicitor’s Email",
        typeOverride = Email,
        access = {DefaultAccess.class}
    )
    private String solicitorEmail;

    @CCD(
        label = "Solicitor Helping With Application",
        access = {DefaultAccess.class}
    )
    private YesOrNo solicitorHelpingWithApplication;

    // BULK SCAN phase 2 changes starts

    @CCD(label = "Address1")
    private String address1;

    @CCD(label = "Address2")
    private String address2;

    @CCD(label = "Town")
    private String addressTown;

    @CCD(label = "Country")
    private String addressCountry;

    @CCD(label = "Post code")
    private String addressPostCode;

    @CCD(label = "Solicitor’s Fax No")
    private String solicitorFaxNumber;

    @CCD(label = "Solicitor’s Dx No")
    private String solicitorDxNumber;

    @CCD(label = "Solicitor’s Fee account no")
    private String solicitorFeeAccountNumber;

    // BULK SCAN phase 2 changes ends
}
