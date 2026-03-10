package uk.gov.hmcts.reform.adoption.notification;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Applicant;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Children;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference.ENGLISH;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.DATE_SUBMITTED;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.HYPHENATED_REF;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.NO;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.YES;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.NOTIFY_APPLICANT_LA_REMINDED_TO_SUBMIT_ALERT;
import static uk.gov.hmcts.reform.adoption.notification.FormatUtil.DATE_TIME_FORMATTER;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.APPLICANT_1_FULL_NAME;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.APPLICANT_2_FULL_NAME;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.CHILD_FULL_NAME;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.HAS_SECOND_APPLICANT;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.LOCAL_COURT_NAME;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(MockitoExtension.class)
public class ApplicantAlertForLaAlertedToSubmitToCourtTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ApplicantAlertForLaAlertedToSubmitToCourt applicantAlertForLaAlertedToSubmitToCourt;

    private CaseData caseData;

    @BeforeEach
    void setUp() {
        caseData = caseData();
        caseData.setHyphenatedCaseRef("1234-2234-3234-4234");
        caseData.getApplication().setDateSubmitted(LocalDate.of(2025, Month.MARCH, 1));
        caseData.setFamilyCourtName(StringUtils.EMPTY);

        Applicant applicant1 = new Applicant();
        applicant1.setFirstName("Applicant1 First");
        applicant1.setLastName("Applicant1 Last");
        applicant1.setEmailAddress("applicant1@applicant.com");
        caseData.setApplicant1(applicant1);
        caseData.setApplicant2(null);

        Children child = new Children();
        child.setFirstName("Child First");
        child.setLastName("Child Last");
        child.setDateOfBirth(LocalDate.of(2023, Month.FEBRUARY, 1));
        caseData.setChildren(child);
    }

    @Test
    void sendExpectedEmailToSingleApplicant() {
        Map<String, Object> expectedTemplateVars = new HashMap<>();
        expectedTemplateVars.put(HYPHENATED_REF, "1234-2234-3234-4234");
        expectedTemplateVars.put(CHILD_FULL_NAME, "Child First Child Last");
        expectedTemplateVars.put(APPLICANT_1_FULL_NAME, "Applicant1 First Applicant1 Last");
        expectedTemplateVars.put(APPLICANT_2_FULL_NAME, StringUtils.EMPTY);
        expectedTemplateVars.put(HAS_SECOND_APPLICANT, NO);
        expectedTemplateVars.put(LOCAL_COURT_NAME, StringUtils.EMPTY);
        expectedTemplateVars.put(DATE_SUBMITTED, "1 March 2025");

        applicantAlertForLaAlertedToSubmitToCourt.sendApplicantAlertForLaAlertedToSubmitToCourt(caseData,
                                                                                                1234223432344234L);

        verify(notificationService, times(1)).sendEmail(
            "applicant1@applicant.com",
            NOTIFY_APPLICANT_LA_REMINDED_TO_SUBMIT_ALERT,
            expectedTemplateVars,
            ENGLISH
        );
    }

    @Test
    void sendExpectedEmailsToTwoApplicants() {
        Applicant applicant2 = new Applicant();
        applicant2.setFirstName("Applicant2 First");
        applicant2.setLastName("Applicant2 Last");
        applicant2.setEmailAddress("applicant2@applicant.com");
        caseData.setApplicant2(applicant2);

        Map<String, Object> expectedTemplateVars = new HashMap<>();
        expectedTemplateVars.put(HYPHENATED_REF, "1234-2234-3234-4234");
        expectedTemplateVars.put(CHILD_FULL_NAME, "Child First Child Last");
        expectedTemplateVars.put(APPLICANT_1_FULL_NAME, "Applicant1 First Applicant1 Last");
        expectedTemplateVars.put(APPLICANT_2_FULL_NAME, "Applicant2 First Applicant2 Last");
        expectedTemplateVars.put(HAS_SECOND_APPLICANT, YES);
        expectedTemplateVars.put(LOCAL_COURT_NAME, StringUtils.EMPTY);
        expectedTemplateVars.put(DATE_SUBMITTED, "1 March 2025");

        applicantAlertForLaAlertedToSubmitToCourt.sendApplicantAlertForLaAlertedToSubmitToCourt(caseData,
                                                                                                1234223432344234L);

        verify(notificationService, times(1)).sendEmail(
            "applicant1@applicant.com",
            NOTIFY_APPLICANT_LA_REMINDED_TO_SUBMIT_ALERT,
            expectedTemplateVars,
            ENGLISH
        );

        verify(notificationService, times(1)).sendEmail(
            "applicant2@applicant.com",
            NOTIFY_APPLICANT_LA_REMINDED_TO_SUBMIT_ALERT,
            expectedTemplateVars,
            ENGLISH
        );
    }

    @Test
    void useTodaysDateWhenDateSubmittedIsNull() {
        caseData.getApplication().setDateSubmitted(null);

        Map<String, Object> expectedTemplateVars = new HashMap<>();
        expectedTemplateVars.put(HYPHENATED_REF, "1234-2234-3234-4234");
        expectedTemplateVars.put(CHILD_FULL_NAME, "Child First Child Last");
        expectedTemplateVars.put(APPLICANT_1_FULL_NAME, "Applicant1 First Applicant1 Last");
        expectedTemplateVars.put(APPLICANT_2_FULL_NAME, StringUtils.EMPTY);
        expectedTemplateVars.put(HAS_SECOND_APPLICANT, NO);
        expectedTemplateVars.put(LOCAL_COURT_NAME, StringUtils.EMPTY);
        expectedTemplateVars.put(DATE_SUBMITTED, LocalDate.now().format(DATE_TIME_FORMATTER));

        applicantAlertForLaAlertedToSubmitToCourt.sendApplicantAlertForLaAlertedToSubmitToCourt(caseData,
                                                                                                1234223432344234L);

        verify(notificationService, times(1)).sendEmail(
            "applicant1@applicant.com",
            NOTIFY_APPLICANT_LA_REMINDED_TO_SUBMIT_ALERT,
            expectedTemplateVars,
            ENGLISH
        );
    }

    @Test
    void doNotSendEmailWhenEmailAddressIsInvalid() {
        caseData.getApplicant1().setEmailAddress("invalid");

        Map<String, Object> expectedTemplateVars = new HashMap<>();
        expectedTemplateVars.put(HYPHENATED_REF, "1234-2234-3234-4234");
        expectedTemplateVars.put(CHILD_FULL_NAME, "Child First Child Last");
        expectedTemplateVars.put(APPLICANT_1_FULL_NAME, "Applicant1 First Applicant1 Last");
        expectedTemplateVars.put(APPLICANT_2_FULL_NAME, StringUtils.EMPTY);
        expectedTemplateVars.put(HAS_SECOND_APPLICANT, NO);
        expectedTemplateVars.put(LOCAL_COURT_NAME, StringUtils.EMPTY);
        expectedTemplateVars.put(DATE_SUBMITTED, "1 March 2025");

        applicantAlertForLaAlertedToSubmitToCourt.sendApplicantAlertForLaAlertedToSubmitToCourt(caseData,
                                                                                                1234223432344234L);

        verify(notificationService, times(0)).sendEmail(
            anyString(),
            any(),
            anyMap(),
            any()
        );
    }
}
