package uk.gov.hmcts.reform.adoption.adoptioncase.validation;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

class RecipientValidationUtilTest {


    @Test
    void isInvalidRecipientForGeneralOrderTest() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        final CaseData caseData = caseDetails.getData();

        assertThat(RecipientValidationUtil.isValidRecipientForGeneralOrderOthers(
           "ABCDEF",
            caseData
        )).isEqualTo(CaseFieldsConstants.ERROR_INVALID_RECIPIENTS_SELECTION);
    }

    @Test
    void isOtherLocalAuthorityValidRecipientForGeneralOrderTest() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        final CaseData caseData = caseDetails.getData();
        caseData.setLocalAuthority(null);
        assertThat(RecipientValidationUtil.isValidRecipientForGeneralOrder(
            CaseFieldsConstants.OTHER_LOCAL_AUTHORITY,
            caseData
        )).isEqualTo(CaseFieldsConstants.OTHER_LA_NOT_APPLICABLE);
    }

    @Test
    void isOtherLocalAuthorityValidRecipientForGeneralOrderTest_2() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        final CaseData caseData = caseDetails.getData();
        assertThat(RecipientValidationUtil.isValidRecipientForGeneralOrder(
            CaseFieldsConstants.OTHER_LOCAL_AUTHORITY,
            caseData
        )).isNull();
    }

    /**
     * Helper method to build CaseDetails .
     *
     * @return - ConfigBuilderImpl
     */
    private CaseDetails<CaseData, State> getCaseDetails() {
        return CaseDetails.<CaseData, State>builder()
            .data(CaseData.builder().build())
            .id(1L)
            .build();
    }

    /**
     * Helper method to build CaseDetails .
     *
     * @return - ConfigBuilderImpl
     */
    private CaseDetails<CaseData, State> getCaseDetailsWithParties() {
        return CaseDetails.<CaseData, State>builder()
            .data(caseData())
            .id(1L)
            .build();
    }

}
