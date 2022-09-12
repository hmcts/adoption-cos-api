package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import uk.gov.hmcts.ccd.sdk.api.CCD;

@Data
@AllArgsConstructor
public class OtherParty {

    @CCD(
        label = "Role",
        displayOrder = 9
    )
    private String otherPartyRole;

    @CCD(
        label = "Name",
        displayOrder = 10
    )
    private String otherPartyName;
}
