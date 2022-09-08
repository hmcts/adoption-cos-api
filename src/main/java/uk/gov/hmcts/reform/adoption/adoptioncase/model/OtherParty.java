package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.CCD;

@Getter
@AllArgsConstructor
public class OtherParty {

    @CCD(
        label = "Role"
    )
    private String otherPartyRole;

    @CCD(
        label = "Name"
    )
    private String otherPartyName;
}
