package uk.gov.hmcts.reform.adoption.notification;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
//import uk.gov.hmcts.reform.adoption.adoptioncase.model.Applicant;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.common.config.EmailTemplatesConfig;
import uk.gov.hmcts.reform.adoption.idam.IdamService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference.ENGLISH;
//import static uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference.WELSH;
//import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.NO;
//import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.YES;
//import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.MULTI_CHILD_SUBMIT_APPLICATION_EMAIL_ALERT;
//import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.*;
//import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.*;
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


    @Test // Will complete this test, and write others, when the semantics finalised.
    void localAuthorityAlertToSubmitToCourtTest_sendLocalAuthorityAlertToSubmitToCourt() {

        CaseData caseData = caseData();
        caseData.setDueDate(LocalDate.of(2021, 4, 21));
        caseData.setFamilyCourtName(StringUtils.EMPTY);

        // For case data:
        // Need to add a child social worker then email address

        // Need to add an applicant social worker then email address

        // Need to add hyphenated case ref

        // Need to add child and fname sname

        // Need to add date submitted / AND A SECOND TEST WITH NO DATE SUBMITTED

        // Need to add LA Portal URL

        // Need to populate template vars, Hardcode them to match the values added to the case.


        // This is code from a different test - use as an example for template vars.
        Map<String, Object> templateVars = new HashMap<>();
        //        templateVars.put(APPLICANT_1_FULL_NAME, caseData.getApplicant1().getFirstName() + " "
        //                + caseData.getApplicant1().getLastName());
        //        if (caseData.getApplicant2() != null) {
        //            templateVars.put(
        //                    APPLICANT_2_FULL_NAME,
        //                    caseData.getApplicant2().getFirstName() + " " + caseData.getApplicant2().getLastName()
        //            );
        //            templateVars.put(HAS_SECOND_APPLICANT, YES);
        //        } else {
        //            templateVars.put(HAS_SECOND_APPLICANT, NO);
        //            templateVars.put(APPLICANT_2_FULL_NAME, StringUtils.EMPTY);
        //        }
        //        templateVars.put(HAS_MULTIPLE_APPLICANT, YES);

        localAuthorityAlertToSubmitToCourt.sendLocalAuthorityAlertToSubmitToCourt(caseData, 1234567890123456L);

        //        verify(notificationService, times(2)).sendEmail(
        //            TEST_USER_EMAIL,
        //            MULTI_CHILD_SUBMIT_APPLICATION_EMAIL_ALERT,
        //            templateVars,
        //            ENGLISH
        //        );
    }
}