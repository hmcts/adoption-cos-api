package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import uk.gov.hmcts.ccd.sdk.api.CCD;

@Data
@AllArgsConstructor
public class OtherParty {

    @CCD(
        label = "Role",
        displayOrder = 0
    )
    private String otherPartyRole;

    @CCD(
        label = "Name",
        displayOrder = 1
    )
    private String otherPartyName;
}
