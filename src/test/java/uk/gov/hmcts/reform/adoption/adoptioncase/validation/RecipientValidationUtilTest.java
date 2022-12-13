package uk.gov.hmcts.reform.adoption.adoptioncase.validation;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.DirectionsOrderData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RecipientValidationUtilTest {


    @Test
    void isApplicantValidRecipientForGeneralOrderTest() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        final CaseData caseData = caseDetails.getData();

        assertThat(RecipientValidationUtil.isValidRecipientForGeneralOrder(
            DirectionsOrderData.GeneralDirectionRecipients.APPLICANT1,
            caseData
        )).isEqualTo(CaseFieldsConstants.FIRST_APPLICANT_NOT_APPLICABLE);
    }

    @Test
    void isApplicant2ValidRecipientForGeneralOrderTest() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        final CaseData caseData = caseDetails.getData();
        assertThat(RecipientValidationUtil.isValidRecipientForGeneralOrder(
            DirectionsOrderData.GeneralDirectionRecipients.APPLICANT2,
            caseData
        )).isEqualTo(CaseFieldsConstants.SECOND_APPLICANT_NOT_APPLICABLE);
    }

    @Test
    void isBirthMotherValidRecipientForGeneralOrderTest() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        final CaseData caseData = caseDetails.getData();
        assertThat(RecipientValidationUtil.isValidRecipientForGeneralOrder(
            DirectionsOrderData.GeneralDirectionRecipients.RESPONDENT_BIRTH_MOTHER,
            caseData
        )).isEqualTo(CaseFieldsConstants.BIRTH_MOTHER_NOT_APPLICABLE);
    }

    @Test
    void isBirthFatherValidRecipientForGeneralOrderTest() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        final CaseData caseData = caseDetails.getData();
        assertThat(RecipientValidationUtil.isValidRecipientForGeneralOrder(
            DirectionsOrderData.GeneralDirectionRecipients.RESPONDENT_BIRTH_FATHER,
            caseData
        )).isEqualTo(CaseFieldsConstants.BIRTH_FATHER_NOT_APPLICABLE);
    }

    @Test
    void isLegalGuardianValidRecipientForGeneralOrderTest() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        final CaseData caseData = caseDetails.getData();
        assertThat(RecipientValidationUtil.isValidRecipientForGeneralOrder(
            DirectionsOrderData.GeneralDirectionRecipients.LEGAL_GUARDIAN_CAFCASS,
            caseData
        )).isEqualTo(CaseFieldsConstants.LEGAL_GUARDIAN_NOT_APPLICABLE);
    }

    @Test
    void isChildLocalAuthorityValidRecipientForGeneralOrderTest() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        final CaseData caseData = caseDetails.getData();
        assertThat(RecipientValidationUtil.isValidRecipientForGeneralOrder(
            DirectionsOrderData.GeneralDirectionRecipients.CHILDS_LOCAL_AUTHORITY,
            caseData
        )).isEqualTo(CaseFieldsConstants.CHILDS_LA_NOT_APPLICABLE);
    }

    @Test
    void isApplicantLocalAuthorityValidRecipientForGeneralOrderTest() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        final CaseData caseData = caseDetails.getData();
        assertThat(RecipientValidationUtil.isValidRecipientForGeneralOrder(
            DirectionsOrderData.GeneralDirectionRecipients.APPLICANTS_LOCAL_AUTHORITY,
            caseData
        )).isEqualTo(CaseFieldsConstants.APPLICANTS_LA_NOT_APPLICABLE);
    }

    @Test
    void isAdoptionAgencyValidRecipientForGeneralOrderTest() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        final CaseData caseData = caseDetails.getData();
        assertThat(RecipientValidationUtil.isValidRecipientForGeneralOrder(
            DirectionsOrderData.GeneralDirectionRecipients.ADOPTION_AGENCY,
            caseData
        )).isEqualTo(CaseFieldsConstants.ADOP_AGENCY_NOT_APPLICABLE);
    }

    @Test
    void isOtherAdoptionAgencyValidRecipientForGeneralOrderTest() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        final CaseData caseData = caseDetails.getData();
        assertThat(RecipientValidationUtil.isValidRecipientForGeneralOrder(
            DirectionsOrderData.GeneralDirectionRecipients.OTHER_ADOPTION_AGENCY,
            caseData
        )).isEqualTo(CaseFieldsConstants.OTHER_ADOP_AGENCY_NOT_APPLICABLE);
    }

    @Test
    void isOtherParentValidRecipientForGeneralOrderTest() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        final CaseData caseData = caseDetails.getData();
        assertThat(RecipientValidationUtil.isValidRecipientForGeneralOrder(
            DirectionsOrderData.GeneralDirectionRecipients.OTHER_PERSON_WITH_PARENTAL_RESPONSIBILITY,
            caseData
        )).isEqualTo(CaseFieldsConstants.OTHER_PARENT_AGENCY_NOT_APPLICABLE);
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

}
