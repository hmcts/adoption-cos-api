package uk.gov.hmcts.reform.adoption.notification;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Children;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.SocialWorker;
import uk.gov.hmcts.reform.adoption.common.config.EmailTemplatesConfig;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference.ENGLISH;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.DATE_SUBMITTED;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.HYPHENATED_REF;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.LOCAL_AUTHORITY_SUBMIT_TO_COURT_ALERT;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.CHILD_FULL_NAME;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.LA_PORTAL_URL;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.LOCAL_COURT_NAME;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_LA_PORTAL_URL;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(MockitoExtension.class)
class LocalAuthorityAlertToSubmitToCourtTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private EmailTemplatesConfig emailTemplatesConfig;

    @InjectMocks
    private LocalAuthorityAlertToSubmitToCourt localAuthorityAlertToSubmitToCourt;

    private CaseData caseData;

    private Map<String, String> templateVarsValues;

    @BeforeEach
    void setUp() {
        templateVarsValues = new HashMap<>();
        templateVarsValues.put(LA_PORTAL_URL, TEST_LA_PORTAL_URL);

        caseData = caseData();
        caseData.setHyphenatedCaseRef("1234-2234-3234-4234");
        // TODO A SECOND TEST WITH NO DATE SUBMITTED
        caseData.getApplication().setDateSubmitted(LocalDate.of(2025, Month.MARCH, 1));
        caseData.setFamilyCourtName(StringUtils.EMPTY);

        SocialWorker childSocialWorker = new SocialWorker();
        childSocialWorker.setLocalAuthorityEmail("child-sw@local-authority.gov.uk");
        caseData.setChildSocialWorker(childSocialWorker);

        SocialWorker applicantSocialWorker = new SocialWorker();
        applicantSocialWorker.setLocalAuthorityEmail("applicant-sw@local-authority.gov.uk");
        caseData.setApplicantSocialWorker(applicantSocialWorker);

        Children child = new Children();
        child.setFirstName("Child First");
        child.setLastName("Child Last");
        child.setDateOfBirth(LocalDate.of(2023, Month.FEBRUARY, 1));
        caseData.setChildren(child);
    }

    @Test
    void localAuthorityAlertToSubmitToCourtTest_sendLocalAuthorityAlertToSubmitToCourt() {
        // Stub LA Portal URL
        when(emailTemplatesConfig.getTemplateVars()).thenReturn(templateVarsValues);

        Map<String, Object> expectedTemplateVars = new HashMap<>();
        expectedTemplateVars.put(HYPHENATED_REF, "1234-2234-3234-4234");
        expectedTemplateVars.put(LOCAL_COURT_NAME, StringUtils.EMPTY);
        expectedTemplateVars.put(CHILD_FULL_NAME, "Child First Child Last");
        expectedTemplateVars.put(DATE_SUBMITTED, "1 March 2025");
        expectedTemplateVars.put(LA_PORTAL_URL, TEST_LA_PORTAL_URL);

        localAuthorityAlertToSubmitToCourt.sendLocalAuthorityAlertToSubmitToCourt(caseData, 1234223432344234L);

        verify(notificationService, times(1)).sendEmail(
            "child-sw@local-authority.gov.uk",
            LOCAL_AUTHORITY_SUBMIT_TO_COURT_ALERT,
            expectedTemplateVars,
            ENGLISH
        );

        verify(notificationService, times(1)).sendEmail(
            "applicant-sw@local-authority.gov.uk",
            LOCAL_AUTHORITY_SUBMIT_TO_COURT_ALERT,
            expectedTemplateVars,
            ENGLISH
        );
    }
}
