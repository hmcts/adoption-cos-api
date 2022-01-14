package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.Email;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.PhoneUK;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Builder
public class AdoptionAgencyOrLocalAuthority {

    @CCD(label = "Adoption Agency Or Local Authority Id")
    private String adopAgencyOrLaId;

    @CCD(label = "Adoption Agency Or Local Authority Name")
    private String adopAgencyOrLaName;

    @CCD(label = "Adoption Agency Or Local Authority Phone Number",
        typeOverride = PhoneUK
    )
    private String adopAgencyOrLaPhoneNumber;

    @CCD(label = "Adoption Agency Or Local Authority Contact Name")
    private String adopAgencyOrLaContactName;

    @CCD(label = "Adoption Agency Or Local Authority Contact Email",
        typeOverride = Email
    )
    private String adopAgencyOrLaContactEmail;
}
