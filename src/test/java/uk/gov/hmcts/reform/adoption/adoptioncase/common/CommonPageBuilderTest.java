package uk.gov.hmcts.reform.adoption.adoptioncase.common;

import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;

import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

public class CommonPageBuilderTest {


    private CaseDetails<CaseData, State> getCaseDetails() {
        final var details = new CaseDetails<CaseData, State>();
        final var data = caseData();
        details.setData(data);
        details.setId(1L);
        return details;
    }
}
