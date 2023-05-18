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
public class LocalAuthority {

    @CCD(label = "Local Authority Name",
        access = {DefaultAccess.class})
    private String localAuthorityName;

    @CCD(label = "Local Authority Contact Name",
        access = {DefaultAccess.class})
    private String localAuthorityContactName;

    @CCD(label = "Local Authority Phone Number",
        access = {DefaultAccess.class}
    )
    private String localAuthorityPhoneNumber;

    @CCD(label = "Local Authority Contact Email",
        typeOverride = Email,
        access = {DefaultAccess.class}
    )
    private String localAuthorityContactEmail;
}
