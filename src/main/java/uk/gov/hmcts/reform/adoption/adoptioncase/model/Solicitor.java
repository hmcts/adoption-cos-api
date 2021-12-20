package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.Email;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.TextArea;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Builder
public class Solicitor {

    @CCD(label = "Solicitor’s name")
    private String name;

    @CCD(label = "Solicitor’s reference number")
    private String reference;

    @CCD(
        label = "Solicitor’s Phone number",
        regex = "^[0-9 +().-]{9,}$"
    )
    private String phone;

    @CCD(
        label = "Solicitor’s Email",
        typeOverride = Email
    )
    private String email;

    @CCD(
        label = "Solicitor’s firm/ DX address",
        typeOverride = TextArea
    )
    private String address;

    @CCD(
        label = "I confirm I am willing to accept service of all "
            + "correspondence and orders by email at the email address "
            + "stated above."
    )
    private YesOrNo agreeToReceiveEmails;
}
