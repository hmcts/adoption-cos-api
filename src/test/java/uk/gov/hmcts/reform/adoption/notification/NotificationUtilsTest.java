package uk.gov.hmcts.reform.adoption.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.SocialWorker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_USER_EMAIL;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_USER_EMAIL_2;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_USER_EMAIL_3;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_USER_EMAIL_4;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

class NotificationUtilsTest {

    private CaseData caseData;

    @BeforeEach
    void setUp() {
        caseData = caseData();
        caseData.setHyphenatedCaseRef("1234-2234-3234-4234");

        SocialWorker childSocialWorker = new SocialWorker();
        caseData.setChildSocialWorker(childSocialWorker);

        SocialWorker applicantSocialWorker = new SocialWorker();
        caseData.setApplicantSocialWorker(applicantSocialWorker);
    }

    @Test
    void shouldReturnEmptySetWhenNoLocalAuthorityEmailsPresent() {
        var result = NotificationUtils.collectUniqueLocalAuthorityEmails(caseData);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptySetWhenLocalAuthorityEmailsSetToNull() {
        caseData.getChildSocialWorker().setLocalAuthorityEmail(null);
        caseData.getChildSocialWorker().setSocialWorkerEmail(null);
        caseData.getApplicantSocialWorker().setLocalAuthorityEmail(null);
        caseData.getApplicantSocialWorker().setSocialWorkerEmail(null);

        var result = NotificationUtils.collectUniqueLocalAuthorityEmails(caseData);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptySetWhenSocialWorkersSetToNull() {
        caseData.setChildSocialWorker(null);
        caseData.setApplicantSocialWorker(null);

        var result = NotificationUtils.collectUniqueLocalAuthorityEmails(caseData);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnSetOfOneValueWhenAllLocalAuthorityEmailsAreSame() {
        caseData.getChildSocialWorker().setLocalAuthorityEmail(TEST_USER_EMAIL);
        caseData.getChildSocialWorker().setSocialWorkerEmail("TEST@hmcts.net");
        caseData.getApplicantSocialWorker().setLocalAuthorityEmail(TEST_USER_EMAIL);
        caseData.getApplicantSocialWorker().setSocialWorkerEmail("Test@HMCTS.net");

        var result = NotificationUtils.collectUniqueLocalAuthorityEmails(caseData);
        assertEquals(1, result.size());
        assertTrue(result.contains(TEST_USER_EMAIL.toLowerCase()));
    }

    @Test
    void shouldReturnSetOfTwoValuesWhenTwoUniqueLocalAuthorityEmailsPresent() {
        caseData.getChildSocialWorker().setLocalAuthorityEmail("child-sw@local-authority.gov.uk");
        caseData.getApplicantSocialWorker().setLocalAuthorityEmail("applicant-sw@local-authority.gov.uk");

        var result = NotificationUtils.collectUniqueLocalAuthorityEmails(caseData);
        assertEquals(2, result.size());
        assertTrue(result.contains("child-sw@local-authority.gov.uk"));
    }

    @Test
    void shouldReturnSetOfFourValuesWhenFourUniqueLocalAuthorityEmailsPresent() {
        caseData.getChildSocialWorker().setLocalAuthorityEmail(TEST_USER_EMAIL);
        caseData.getChildSocialWorker().setSocialWorkerEmail(TEST_USER_EMAIL_2);
        caseData.getApplicantSocialWorker().setLocalAuthorityEmail(TEST_USER_EMAIL_3);
        caseData.getApplicantSocialWorker().setSocialWorkerEmail(TEST_USER_EMAIL_4);

        var result = NotificationUtils.collectUniqueLocalAuthorityEmails(caseData);
        assertEquals(4, result.size());
    }
}
