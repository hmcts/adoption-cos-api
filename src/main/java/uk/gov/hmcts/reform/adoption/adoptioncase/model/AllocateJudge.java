package uk.gov.hmcts.reform.adoption.adoptioncase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.DefaultAccess;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllocateJudge {

    @CCD(label = "Name of the judge",
        access = {DefaultAccess.class})
    private String judgeName;
}
