package uk.gov.hmcts.reform.adoption.adoptioncase.validation;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.DirectionsOrderData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.SocialWorker;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.AdoptionAgencyOrLocalAuthority;
import uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

class RecipientValidationUtilTest {


    @Test
    void isApplicantValidRecipientForGeneralOrderTest() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        final CaseData caseData = caseDetails.getData();

        assertThat(RecipientValidationUtil.isValidRecipientForGeneralOrder(
            DirectionsOrderData.GeneralDirectionRecipients.APPLICANT1.name(),
            caseData
        )).isEqualTo(CaseFieldsConstants.FIRST_APPLICANT_NOT_APPLICABLE);
    }


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
    void isApplicantValidRecipientForGeneralOrderTest_2() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetailsWithParties();
        final CaseData caseData = caseDetails.getData();

        assertThat(RecipientValidationUtil.isValidRecipientForGeneralOrder(
            DirectionsOrderData.GeneralDirectionRecipients.APPLICANT1.name(),
            caseData
        )).isNull();
    }

    @Test
    void isApplicant2ValidRecipientForGeneralOrderTest() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        final CaseData caseData = caseDetails.getData();
        assertThat(RecipientValidationUtil.isValidRecipientForGeneralOrder(
            DirectionsOrderData.GeneralDirectionRecipients.APPLICANT2.name(),
            caseData
        )).isEqualTo(CaseFieldsConstants.SECOND_APPLICANT_NOT_APPLICABLE);
    }

    @Test
    void isApplicant2ValidRecipientForGeneralOrderTest_2() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetailsWithParties();
        final CaseData caseData = caseDetails.getData();
        assertThat(RecipientValidationUtil.isValidRecipientForGeneralOrder(
            DirectionsOrderData.GeneralDirectionRecipients.APPLICANT2.name(),
            caseData
        )).isNull();
    }

    @Test
    void isBirthMotherValidRecipientForGeneralOrderTest() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        final CaseData caseData = caseDetails.getData();
        assertThat(RecipientValidationUtil.isValidRecipientForGeneralOrder(
            DirectionsOrderData.GeneralDirectionRecipients.RESPONDENT_BIRTH_MOTHER.name(),
            caseData
        )).isEqualTo(CaseFieldsConstants.BIRTH_MOTHER_NOT_APPLICABLE);
    }

    @Test
    void isBirthFatherValidRecipientForGeneralOrderTest() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        final CaseData caseData = caseDetails.getData();
        assertThat(RecipientValidationUtil.isValidRecipientForGeneralOrder(
            DirectionsOrderData.GeneralDirectionRecipients.RESPONDENT_BIRTH_FATHER.name(),
            caseData
        )).isEqualTo(CaseFieldsConstants.BIRTH_FATHER_NOT_APPLICABLE);
    }

    @Test
    void isLegalGuardianValidRecipientForGeneralOrderTest() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        final CaseData caseData = caseDetails.getData();
        assertThat(RecipientValidationUtil.isValidRecipientForGeneralOrder(
            DirectionsOrderData.GeneralDirectionRecipients.LEGAL_GUARDIAN_CAFCASS.name(),
            caseData
        )).isEqualTo(CaseFieldsConstants.LEGAL_GUARDIAN_NOT_APPLICABLE);
    }

    @Test
    void isLegalGuardianValidRecipientForGeneralOrderTest_2() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        final CaseData caseData = caseDetails.getData();
        caseData.setIsChildRepresentedByGuardian(YesOrNo.YES);
        assertThat(RecipientValidationUtil.isValidRecipientForGeneralOrder(
            DirectionsOrderData.GeneralDirectionRecipients.LEGAL_GUARDIAN_CAFCASS.name(),
            caseData
        )).isNull();
    }

    @Test
    void isChildLocalAuthorityValidRecipientForGeneralOrderTest() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        final CaseData caseData = caseDetails.getData();
        assertThat(RecipientValidationUtil.isValidRecipientForGeneralOrder(
            DirectionsOrderData.GeneralDirectionRecipients.CHILDS_LOCAL_AUTHORITY.name(),
            caseData
        )).isEqualTo(CaseFieldsConstants.CHILDS_LA_NOT_APPLICABLE);
    }

    @Test
    void isChildLocalAuthorityValidRecipientForGeneralOrderTest_2() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        final CaseData caseData = caseDetails.getData();
        SocialWorker socialWorker = new SocialWorker();
        socialWorker.setLocalAuthority("Local Authority");
        caseData.setChildSocialWorker(socialWorker);
        assertThat(RecipientValidationUtil.isValidRecipientForGeneralOrder(
            DirectionsOrderData.GeneralDirectionRecipients.CHILDS_LOCAL_AUTHORITY.name(),
            caseData
        )).isNull();
    }

    @Test
    void isApplicantLocalAuthorityValidRecipientForGeneralOrderTest() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        final CaseData caseData = caseDetails.getData();
        assertThat(RecipientValidationUtil.isValidRecipientForGeneralOrder(
            DirectionsOrderData.GeneralDirectionRecipients.APPLICANTS_LOCAL_AUTHORITY.name(),
            caseData
        )).isEqualTo(CaseFieldsConstants.APPLICANTS_LA_NOT_APPLICABLE);
    }

    @Test
    void isApplicantLocalAuthorityValidRecipientForGeneralOrderTest_2() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        final CaseData caseData = caseDetails.getData();
        SocialWorker socialWorker = new SocialWorker();
        socialWorker.setLocalAuthority("Local Authority");
        caseData.setApplicantSocialWorker(socialWorker);
        assertThat(RecipientValidationUtil.isValidRecipientForGeneralOrder(
            DirectionsOrderData.GeneralDirectionRecipients.APPLICANTS_LOCAL_AUTHORITY.name(),
            caseData
        )).isNull();
    }

    @Test
    void isAdoptionAgencyValidRecipientForGeneralOrderTest() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        final CaseData caseData = caseDetails.getData();
        assertThat(RecipientValidationUtil.isValidRecipientForGeneralOrder(
            DirectionsOrderData.GeneralDirectionRecipients.ADOPTION_AGENCY.name(),
            caseData
        )).isEqualTo(CaseFieldsConstants.ADOP_AGENCY_NOT_APPLICABLE);
    }

    @Test
    void isAdoptionAgencyValidRecipientForGeneralOrderTest_2() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        final CaseData caseData = caseDetails.getData();
        AdoptionAgencyOrLocalAuthority adoptionAgencyOrLA = new AdoptionAgencyOrLocalAuthority();
        adoptionAgencyOrLA.setAdopAgencyOrLaName("Local Authority");
        caseData.setAdopAgencyOrLA(adoptionAgencyOrLA);
        assertThat(RecipientValidationUtil.isValidRecipientForGeneralOrder(
            DirectionsOrderData.GeneralDirectionRecipients.ADOPTION_AGENCY.name(),
            caseData
        )).isNull();
    }

    @Test
    void isOtherAdoptionAgencyValidRecipientForGeneralOrderTest() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        final CaseData caseData = caseDetails.getData();
        assertThat(RecipientValidationUtil.isValidRecipientForGeneralOrder(
            DirectionsOrderData.GeneralDirectionRecipients.OTHER_ADOPTION_AGENCY.name(),
            caseData
        )).isEqualTo(CaseFieldsConstants.OTHER_ADOP_AGENCY_NOT_APPLICABLE);
    }

    @Test
    void isOtherAdoptionAgencyValidRecipientForGeneralOrderTest_2() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        final CaseData caseData = caseDetails.getData();
        caseData.setHasAnotherAdopAgencyOrLA(YesOrNo.YES);
        caseData.setHasAnotherAdopAgencyOrLAinXui(YesOrNo.YES);
        assertThat(RecipientValidationUtil.isValidRecipientForGeneralOrder(
            DirectionsOrderData.GeneralDirectionRecipients.OTHER_ADOPTION_AGENCY.name(),
            caseData
        )).isNull();
    }

    @Test
    void isOtherParentValidRecipientForGeneralOrderTest() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        final CaseData caseData = caseDetails.getData();
        caseData.setIsThereAnyOtherPersonWithParentalResponsibility(YesOrNo.NO);
        assertThat(RecipientValidationUtil.isValidRecipientForGeneralOrder(
            DirectionsOrderData.GeneralDirectionRecipients.OTHER_PERSON_WITH_PARENTAL_RESPONSIBILITY.name(),
            caseData
        )).isEqualTo(CaseFieldsConstants.OTHER_PARENT_AGENCY_NOT_APPLICABLE);
    }

    @Test
    void isOtherParentValidRecipientForGeneralOrderTest_2() {
        final CaseDetails<CaseData, State> caseDetails = getCaseDetails();
        final CaseData caseData = caseDetails.getData();
        caseData.setIsThereAnyOtherPersonWithParentalResponsibility(YesOrNo.YES);
        caseData.setIsChildRepresentedByGuardian(YesOrNo.YES);
        assertThat(RecipientValidationUtil.isValidRecipientForGeneralOrder(
            DirectionsOrderData.GeneralDirectionRecipients.OTHER_PERSON_WITH_PARENTAL_RESPONSIBILITY.name(),
            caseData
        )).isNull();
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
