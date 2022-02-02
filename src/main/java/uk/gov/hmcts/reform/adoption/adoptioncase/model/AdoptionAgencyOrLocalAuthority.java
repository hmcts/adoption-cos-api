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

    @CCD(label = "Adoption Agency Or Local Authority Id",
        access = {DefaultAccess.class})
    private String adopAgencyOrLaId;

    @CCD(label = "Adoption Agency Or Local Authority Name",
        access = {DefaultAccess.class})
    private String adopAgencyOrLaName;

    @CCD(label = "Adoption Agency Or Local Authority Phone Number",
        access = {DefaultAccess.class}
    )
    private String adopAgencyOrLaPhoneNumber;

    @CCD(label = "Adoption Agency Or Local Authority Contact Name",
        access = {DefaultAccess.class})
    private String adopAgencyOrLaContactName;

    @CCD(label = "Adoption Agency Or Local Authority Contact Email",
        typeOverride = Email,
        access = {DefaultAccess.class}
    )
    private String adopAgencyOrLaContactEmail;
}
