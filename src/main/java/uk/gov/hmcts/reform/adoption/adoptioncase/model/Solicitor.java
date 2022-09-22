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
public class Solicitor {

    @CCD(label = "Solicitor",
        access = {DefaultAccess.class})
    private String solicitorFirm;

    @CCD(
        label = "Solicitor reference number",
        access = {DefaultAccess.class}
    )
    private String solicitorReference;

    @CCD(
        label = "Address line 1",
        access = {DefaultAccess.class}
    )
    private String address1;

    @CCD(
        label = "Address line 2 (optional)",
        access = {DefaultAccess.class}
    )
    private String address2;

    @CCD(
        label = "Town or city",
        access = {DefaultAccess.class}
    )
    private String addressTown;

    @CCD(
        label = "County (optional)",
        access = {DefaultAccess.class}
    )
    private String addressCounty;

    @CCD(
        label = "Postcode",
        access = {DefaultAccess.class}
    )
    private String addressPostCode;

    @CCD(
        label = "Email address",
        typeOverride = Email,
        access = {DefaultAccess.class}
    )
    private String solicitorEmail;

    @CCD(
        label = "Phone number",
        access = {DefaultAccess.class}
    )
    private String solicitorPhoneNumber;
}
