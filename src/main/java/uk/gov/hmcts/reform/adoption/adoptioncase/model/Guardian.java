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
public class Guardian {


    @CCD(label = "Full name of contact",
        access = {DefaultAccess.class})
    private String name;

    @CCD(
        label = "Email",
        typeOverride = Email
    )
    private String email;

    @CCD(label = "Phone number")
    private String phoneNumber;

    @CCD(access = {DefaultAccess.class},
        label = "Address")
    private AddressUK guardianAddress;

}
