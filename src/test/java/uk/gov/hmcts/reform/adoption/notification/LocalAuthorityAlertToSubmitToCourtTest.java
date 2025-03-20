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
import uk.gov.hmcts.reform.adoption.idam.IdamService;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference.ENGLISH;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.BLANK_SPACE;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.DATE_SUBMITTED;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.HYPHENATED_REF;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.LOCAL_AUTHORITY_SUBMIT_TO_COURT_ALERT;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.CHILD_FULL_NAME;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.LA_PORTAL_URL;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.LOCAL_COURT_NAME;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(MockitoExtension.class)
class LocalAuthorityAlertToSubmitToCourtTest {


    @Mock
    IdamService idamService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private CommonContent commonContent;

    @Mock
    private EmailTemplatesConfig emailTemplatesConfig;

    @InjectMocks
    private LocalAuthorityAlertToSubmitToCourt localAuthorityAlertToSubmitToCourt;

    private CaseData caseData;

    @BeforeEach
    void setUp() {
        caseData = caseData();
        caseData.setDueDate(LocalDate.of(2021, 4, 21));
        caseData.setFamilyCourtName(StringUtils.EMPTY);

        // For case data:
        // Need to add a child social worker then email address
        SocialWorker childSocialWorker = new SocialWorker();
        childSocialWorker.setLocalAuthorityEmail("child-sw@local-authority.gov.uk");
        caseData.setChildSocialWorker(childSocialWorker);

        // Need to add an applicant social worker then email address
        SocialWorker applicantSocialWorker = new SocialWorker();
        applicantSocialWorker.setLocalAuthorityEmail("applicant-sw@local-authority.gov.uk");
        caseData.setApplicantSocialWorker(applicantSocialWorker);

        // Need to add hyphenated case ref
        caseData.setHyphenatedCaseRef("1234-2234-3234-4234");

        // Need to add child and fname sname
        Children child = new Children();
        child.setFirstName("Child First");
        child.setLastName("Child Last");
        child.setDateOfBirth(LocalDate.of(2023, Month.FEBRUARY, 1));
        caseData.setChildren(child);

        // Need to add date submitted / AND TODO A SECOND TEST WITH NO DATE SUBMITTED
        caseData.getApplication().setDateSubmitted(LocalDate.of(2025, Month.MARCH, 1));

        // TODO Need to add LA Portal URL???

    }

    @Test
    void localAuthorityAlertToSubmitToCourtTest_sendLocalAuthorityAlertToSubmitToCourt() {
        // Need to populate template vars, Hardcode them to match the values added to the case.
        Map<String, Object> templateVars = new HashMap<>();
        templateVars.put(HYPHENATED_REF, "1234-2234-3234-4234");
        templateVars.put(LOCAL_COURT_NAME, StringUtils.EMPTY);
        templateVars.put(CHILD_FULL_NAME, "Child First" + BLANK_SPACE + "Child Last");
        templateVars.put(DATE_SUBMITTED, "1 March 2025");
        templateVars.put(LA_PORTAL_URL, /*TEST_LA_PORTAL_URL*/ null); //TODO

        localAuthorityAlertToSubmitToCourt.sendLocalAuthorityAlertToSubmitToCourt(caseData, 1234223432344234L);

        verify(notificationService, times(1)).sendEmail(
            "child-sw@local-authority.gov.uk",
            LOCAL_AUTHORITY_SUBMIT_TO_COURT_ALERT,
            templateVars,
            ENGLISH
        );

        verify(notificationService, times(1)).sendEmail(
            "applicant-sw@local-authority.gov.uk",
            LOCAL_AUTHORITY_SUBMIT_TO_COURT_ALERT,
            templateVars,
            ENGLISH
        );
    }
}
