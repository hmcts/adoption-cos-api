package uk.gov.hmcts.reform.adoption.notification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Applicant;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.testutil.TestDataHelper;

import java.util.HashMap;
import java.util.Map;

import static java.time.Month.APRIL;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference.ENGLISH;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference.WELSH;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.NO;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.MULTI_CHILD_SUBMIT_APPLICATION_EMAIL_ALERT;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.APPLICANT_1_FULL_NAME;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.APPLICANT_2_FULL_NAME;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.HAS_MULTIPLE_APPLICANT;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_FIRST_NAME;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_LAST_NAME;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_USER_EMAIL;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_USER_EMAIL_2;

@ExtendWith(MockitoExtension.class)
class MultiChildSubmitAlertEmailNotificationTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private MultiChildSubmitAlertEmailNotification multiChildSubmitAlertEmailNotification;

    @Test
    void draftApplicationWithApplicant1_whenEmailAddress_sendsEmailToApplicant1EmailAddress() {
        Applicant applicant = TestDataHelper.getApplicant();
        applicant.setEmail(TEST_USER_EMAIL_2);

        final CaseData caseData = CaseData.builder()
            .applicant1(applicant)
            .build();

        multiChildSubmitAlertEmailNotification.sendToApplicants(caseData, 1234567890123456L);

        Map<String, Object> expectedTemplateVars = new HashMap<>();
        expectedTemplateVars.put(APPLICANT_1_FULL_NAME, TEST_FIRST_NAME + " " + TEST_LAST_NAME);
        expectedTemplateVars.put(HAS_MULTIPLE_APPLICANT, NO);
        expectedTemplateVars.put(APPLICANT_2_FULL_NAME, "");

        verify(notificationService, times(1)).sendEmail(
            TEST_USER_EMAIL,
            MULTI_CHILD_SUBMIT_APPLICATION_EMAIL_ALERT,
            expectedTemplateVars,
            ENGLISH
        );
    }

    @Test
    void draftApplicationWithApplicant1_whenNoEmailAddress_sendsEmailToApplicant1Email() {
        Applicant applicant = TestDataHelper.getApplicant();
        applicant.setEmailAddress(null);

        final CaseData caseData = CaseData.builder()
            .applicant1(applicant)
            .build();

        multiChildSubmitAlertEmailNotification.sendToApplicants(caseData, 1234567890123456L);

        Map<String, Object> expectedTemplateVars = new HashMap<>();
        expectedTemplateVars.put(APPLICANT_1_FULL_NAME, TEST_FIRST_NAME + " " + TEST_LAST_NAME);
        expectedTemplateVars.put(HAS_MULTIPLE_APPLICANT, NO);
        expectedTemplateVars.put(APPLICANT_2_FULL_NAME, "");

        verify(notificationService, times(1)).sendEmail(
            TEST_USER_EMAIL,
            MULTI_CHILD_SUBMIT_APPLICATION_EMAIL_ALERT,
            expectedTemplateVars,
            ENGLISH
        );
    }

    @Test
    void draftApplicationWithApplicant1_whenNoEmailAddressAndWelshPreference_sendsEmailToApplicant1EmailWelsh() {
        Applicant applicant = TestDataHelper.getApplicant();
        applicant.setLanguagePreference(WELSH);
        applicant.setEmailAddress(null);

        final CaseData caseData = CaseData.builder()
            .applicant1(applicant)
            .build();

        multiChildSubmitAlertEmailNotification.sendToApplicants(caseData, 1234567890123456L);

        Map<String, Object> expectedTemplateVars = new HashMap<>();
        expectedTemplateVars.put(APPLICANT_1_FULL_NAME, TEST_FIRST_NAME + " " + TEST_LAST_NAME);
        expectedTemplateVars.put(HAS_MULTIPLE_APPLICANT, NO);
        expectedTemplateVars.put(APPLICANT_2_FULL_NAME, "");

        verify(notificationService, times(1)).sendEmail(
            TEST_USER_EMAIL,
            MULTI_CHILD_SUBMIT_APPLICATION_EMAIL_ALERT,
            expectedTemplateVars,
            WELSH
        );
    }

    @Test
    void draftApplicationWithApplicant1_whenNoNameAndNoLanguagePreference_usesEnglishDefault() {
        Applicant applicant = TestDataHelper.getApplicant();
        applicant.setFirstName(null);
        applicant.setLastName(null);
        applicant.setLanguagePreference(null);

        final CaseData caseData = CaseData.builder()
            .applicant1(applicant)
            .build();

        multiChildSubmitAlertEmailNotification.sendToApplicants(caseData, 1234567890123456L);

        Map<String, Object> expectedTemplateVars = new HashMap<>();
        expectedTemplateVars.put(APPLICANT_1_FULL_NAME, "applicant");
        expectedTemplateVars.put(HAS_MULTIPLE_APPLICANT, NO);
        expectedTemplateVars.put(APPLICANT_2_FULL_NAME, "");

        verify(notificationService, times(1)).sendEmail(
            TEST_USER_EMAIL,
            MULTI_CHILD_SUBMIT_APPLICATION_EMAIL_ALERT,
            expectedTemplateVars,
            ENGLISH
        );
    }

    @Test
    void draftApplicationWithApplicant1_whenNoNameAndWelshLanguagePreference_usesWelshDefault() {
        Applicant applicant = TestDataHelper.getApplicant();
        applicant.setFirstName(null);
        applicant.setLastName(null);
        applicant.setLanguagePreference(WELSH);

        final CaseData caseData = CaseData.builder()
            .applicant1(applicant)
            .build();

        multiChildSubmitAlertEmailNotification.sendToApplicants(caseData, 1234567890123456L);

        Map<String, Object> expectedTemplateVars = new HashMap<>();
        expectedTemplateVars.put(APPLICANT_1_FULL_NAME, "ymgeisydd");
        expectedTemplateVars.put(HAS_MULTIPLE_APPLICANT, NO);
        expectedTemplateVars.put(APPLICANT_2_FULL_NAME, "");

        verify(notificationService, times(1)).sendEmail(
            TEST_USER_EMAIL,
            MULTI_CHILD_SUBMIT_APPLICATION_EMAIL_ALERT,
            expectedTemplateVars,
            WELSH
        );
    }

    @Test
    void draftApplicationWithApplicant1_whenNoLastName_usesFirstName() {
        Applicant applicant = TestDataHelper.getApplicant();
        applicant.setLastName(null);
        applicant.setLanguagePreference(WELSH);

        final CaseData caseData = CaseData.builder()
            .applicant1(applicant)
            .build();

        multiChildSubmitAlertEmailNotification.sendToApplicants(caseData, 1234567890123456L);

        Map<String, Object> expectedTemplateVars = new HashMap<>();
        expectedTemplateVars.put(APPLICANT_1_FULL_NAME, TEST_FIRST_NAME);
        expectedTemplateVars.put(HAS_MULTIPLE_APPLICANT, NO);
        expectedTemplateVars.put(APPLICANT_2_FULL_NAME, "");

        verify(notificationService, times(1)).sendEmail(
            TEST_USER_EMAIL,
            MULTI_CHILD_SUBMIT_APPLICATION_EMAIL_ALERT,
            expectedTemplateVars,
            WELSH
        );
    }

    @Test
    void draftApplicationWithApplicant1_whenNoFirstName_usesLastName() {
        Applicant applicant = TestDataHelper.getApplicant();
        applicant.setFirstName(null);

        final CaseData caseData = CaseData.builder()
            .applicant1(applicant)
            .build();

        multiChildSubmitAlertEmailNotification.sendToApplicants(caseData, 1234567890123456L);

        Map<String, Object> expectedTemplateVars = new HashMap<>();
        expectedTemplateVars.put(APPLICANT_1_FULL_NAME, TEST_LAST_NAME);
        expectedTemplateVars.put(HAS_MULTIPLE_APPLICANT, NO);
        expectedTemplateVars.put(APPLICANT_2_FULL_NAME, "");

        verify(notificationService, times(1)).sendEmail(
            TEST_USER_EMAIL,
            MULTI_CHILD_SUBMIT_APPLICATION_EMAIL_ALERT,
            expectedTemplateVars,
            ENGLISH
        );
    }

    @Test
    void draftApplication_whenApplicant2WithNoEmailAddress_sendsEmailToApplicant1Only() {
        final Applicant applicant2 = Applicant.builder()
            .firstName("Second")
            .lastName("Applicant")
            .emailAddress(null)
            .languagePreference(WELSH)
            .build();

        final CaseData caseData = CaseData.builder()
            .applicant1(TestDataHelper.getApplicant())
            .applicant2(applicant2)
            .build();

        multiChildSubmitAlertEmailNotification.sendToApplicants(caseData, 1234567890123456L);

        Map<String, Object> expectedTemplateVars = new HashMap<>();
        expectedTemplateVars.put(APPLICANT_1_FULL_NAME, TEST_FIRST_NAME + " " + TEST_LAST_NAME);
        expectedTemplateVars.put(HAS_MULTIPLE_APPLICANT, NO);
        expectedTemplateVars.put(APPLICANT_2_FULL_NAME, "");

        verify(notificationService, times(1)).sendEmail(
            TEST_USER_EMAIL,
            MULTI_CHILD_SUBMIT_APPLICATION_EMAIL_ALERT,
            expectedTemplateVars,
            ENGLISH
        );
    }

    @Test
    void draftApplication_whenApplicant2_sendsEmailToBothApplicants() {
        final Applicant applicant2 = Applicant.builder()
            .firstName("Second")
            .lastName("Applicant")
            .emailAddress(TEST_USER_EMAIL_2)
            .languagePreference(WELSH)
            .build();

        final CaseData caseData = CaseData.builder()
            .applicant1(TestDataHelper.getApplicant())
            .applicant2(applicant2)
            .build();

        multiChildSubmitAlertEmailNotification.sendToApplicants(caseData, 1234567890123456L);

        Map<String, Object> applicant1ExpectedTemplateVars = new HashMap<>();
        applicant1ExpectedTemplateVars.put(APPLICANT_1_FULL_NAME, TEST_FIRST_NAME + " " + TEST_LAST_NAME);
        applicant1ExpectedTemplateVars.put(HAS_MULTIPLE_APPLICANT, NO);
        applicant1ExpectedTemplateVars.put(APPLICANT_2_FULL_NAME, "");

        Map<String, Object> applicant2ExpectedTemplateVars = new HashMap<>();
        applicant2ExpectedTemplateVars.put(APPLICANT_1_FULL_NAME, "Second Applicant");
        applicant2ExpectedTemplateVars.put(HAS_MULTIPLE_APPLICANT, NO);
        applicant2ExpectedTemplateVars.put(APPLICANT_2_FULL_NAME, "");

        verify(notificationService, times(1)).sendEmail(
            TEST_USER_EMAIL,
            MULTI_CHILD_SUBMIT_APPLICATION_EMAIL_ALERT,
            applicant1ExpectedTemplateVars,
            ENGLISH
        );

        verify(notificationService, times(1)).sendEmail(
            TEST_USER_EMAIL_2,
            MULTI_CHILD_SUBMIT_APPLICATION_EMAIL_ALERT,
            applicant2ExpectedTemplateVars,
            WELSH
        );
    }
}
