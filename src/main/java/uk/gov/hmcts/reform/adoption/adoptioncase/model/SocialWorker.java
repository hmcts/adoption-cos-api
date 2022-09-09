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

    @CCD(label = "Social Worker Name",
        access = {DefaultAccess.class})
    private String socialWorkerName;

    @CCD(label = "Social Worker PhoneNumber",
        access = {DefaultAccess.class}
    )
    private String socialWorkerPhoneNumber;

    @CCD(label = "Social Worker Email",
        typeOverride = Email,
        access = {DefaultAccess.class})
    private String socialWorkerEmail;

    @CCD(label = "Local authority",
        access = {DefaultAccess.class}
    )
    private String localAuthority;

    @CCD(label = "Local authority email address",
        typeOverride = Email,
        access = {DefaultAccess.class}
    )
    private String localAuthorityEmail;
}
