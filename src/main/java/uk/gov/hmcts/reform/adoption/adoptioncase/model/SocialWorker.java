package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
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
        access = {DefaultAccess.class})
    private String socialWorkerEmail;

    @CCD(label = "Social Worker Team Email",
        access = {DefaultAccess.class}
    )
    private String socialWorkerTeamEmail;
}
