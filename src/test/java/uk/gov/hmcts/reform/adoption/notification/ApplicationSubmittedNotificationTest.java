package uk.gov.hmcts.reform.adoption.notification;

import org.apache.commons.lang.StringUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Children;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.SocialWorker;
import uk.gov.hmcts.reform.adoption.common.config.EmailTemplatesConfig;
import uk.gov.hmcts.reform.adoption.document.CaseDocumentClient;
import uk.gov.hmcts.reform.adoption.document.DocumentType;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionDocument;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.service.notify.NotificationClientException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.allOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference.ENGLISH;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.HYPHENATED_REF;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.NO;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.YES;
import static uk.gov.hmcts.reform.adoption.notification.CommonContent.APPLICATION_REFERENCE;
import static uk.gov.hmcts.reform.adoption.notification.CommonContent.SUBMISSION_RESPONSE_DATE;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.APPLICANT_APPLICATION_SUBMITTED;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.APPLICATION_SUBMITTED_TO_LOCAL_AUTHORITY;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.LOCAL_AUTHORITY_APPLICATION_SUBMITTED;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.LOCAL_AUTHORITY_APPLICATION_SUBMITTED_ACKNOWLEDGE_CITIZEN;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.ADOPTION_CUI_MULTI_CHILDREN_URL;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.LA_PORTAL_URL;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_LA_PORTAL_URL;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_USER_EMAIL;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.getMainTemplateVars;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.CHILD_FULL_NAME;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.APPLICANT_1_FULL_NAME;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.APPLICANT_2_FULL_NAME;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.HAS_SECOND_APPLICANT;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.LOCAL_COURT_NAME;

@ExtendWith(MockitoExtension.class)
class ApplicationSubmittedNotificationTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private CommonContent commonContent;

    @Mock
    private IdamService idamService;

    @Mock
    private AuthTokenGenerator authTokenGenerator;

    @Mock
    private CaseDocumentClient caseDocumentClient;

    @Mock
    private EmailTemplatesConfig emailTemplatesConfig;

    @InjectMocks
    private ApplicationSubmittedNotification notification;

    @Mock
    SendgridService sendgridService;


    @Test
    void shouldSendEmailToApplicantsWithSubmissionResponseDate() {
        CaseData caseData = caseData();
        caseData.setDueDate(LocalDate.of(2021, 4, 21));
        when(commonContent.mainTemplateVars(caseData, 1234567890123456L, caseData.getApplicant1(), caseData.getApplicant2()))
            .thenReturn(getMainTemplateVars());
        caseData.setFamilyCourtName(StringUtils.EMPTY);
        Map<String, Object> templateVars = new HashMap<>();
        templateVars.put(HYPHENATED_REF, caseData.getHyphenatedCaseRef());
        templateVars.put(SUBMISSION_RESPONSE_DATE, "21 April 2021");
        templateVars.put(APPLICATION_REFERENCE, "1234-5678-9012-3456");
        templateVars.put(APPLICANT_1_FULL_NAME, caseData.getApplicant1().getFirstName() + " "
            + caseData.getApplicant1().getLastName());
        templateVars.put(LOCAL_COURT_NAME, caseData.getFamilyCourtName());
        if (caseData.getApplicant2() != null) {
            templateVars.put(
                APPLICANT_2_FULL_NAME,
                caseData.getApplicant2().getFirstName() + " " + caseData.getApplicant2().getLastName()
            );
            templateVars.put(HAS_SECOND_APPLICANT, YES);
        } else {
            templateVars.put(HAS_SECOND_APPLICANT, NO);
            templateVars.put(APPLICANT_2_FULL_NAME, StringUtils.EMPTY);
        }
        templateVars.put(ADOPTION_CUI_MULTI_CHILDREN_URL, emailTemplatesConfig.getTemplateVars().get(ADOPTION_CUI_MULTI_CHILDREN_URL));

        notification.sendToApplicants(caseData, 1234567890123456L);

        verify(notificationService, times(2)).sendEmail(
            eq(TEST_USER_EMAIL),
            eq(APPLICANT_APPLICATION_SUBMITTED),
            eq(templateVars),
            eq(ENGLISH)
        );
        verify(commonContent).mainTemplateVars(caseData, 1234567890123456L, caseData.getApplicant1(), caseData.getApplicant2());
    }

    @Test
    void shouldSendEmailToCaseWorkerWithSubmissionResponseDate() {
        CaseData data = caseData();
        data.setDueDate(LocalDate.of(2021, 4, 21));
        when(commonContent.mainTemplateVars(data, 1234567890123456L, data.getApplicant1(), data.getApplicant2()))
            .thenReturn(getMainTemplateVars());

        notification.sendToCaseWorker(data, 1234567890123456L);

        Object submissionResponseDate = new String("21 April 2021");
        Object applicationReference = new String("1234-5678-9012-3456");

        verify(notificationService).sendEmail(
            eq(TEST_USER_EMAIL),
            eq(APPLICANT_APPLICATION_SUBMITTED),
            argThat(allOf(
                Matchers.hasEntry(SUBMISSION_RESPONSE_DATE, submissionResponseDate),
                Matchers.hasEntry(APPLICATION_REFERENCE, applicationReference)
            )),
            eq(ENGLISH)
        );
        verify(commonContent).mainTemplateVars(data, 1234567890123456L, data.getApplicant1(), data.getApplicant2());
    }

    @Test
    void shouldSendEmailToLocalAuthorityPostApplicantSubmission() {
        CaseData data = caseData();
        Children children = new Children();
        children.setFirstName("MOCK_FIRST_NAME");
        children.setLastName("MOCK_LAST_NAME");
        data.setChildren(children);
        SocialWorker socialWorker = new SocialWorker();
        socialWorker.setLocalAuthorityEmail(TEST_USER_EMAIL);
        data.setChildSocialWorker(socialWorker);
        data.setApplicantSocialWorker(socialWorker);
        Map<String, Object> templateVars = new HashMap<>();
        emailTemplatesConfig.getTemplateVars().put(LA_PORTAL_URL, TEST_LA_PORTAL_URL);
        templateVars.put(HYPHENATED_REF, data.getHyphenatedCaseRef());
        templateVars.put(CHILD_FULL_NAME, data.getChildren().getFirstName() + " " + data.getChildren().getLastName());
        templateVars.put(LA_PORTAL_URL, emailTemplatesConfig.getTemplateVars().get(LA_PORTAL_URL));

        notification.sendToLocalAuthorityPostApplicantSubmission(data, 1234567890123456L);

        verify(notificationService, times(2)).sendEmail(
            TEST_USER_EMAIL,
            APPLICATION_SUBMITTED_TO_LOCAL_AUTHORITY,
            templateVars,
            ENGLISH
        );
    }

    @Test
    void shouldSendEmailToLocalAuthorityPostLocalAuthoritySubmission() {
        CaseData data = caseData();
        Children children = new Children();
        children.setFirstName("MOCK_FIRST_NAME");
        children.setLastName("MOCK_LAST_NAME");
        data.setChildren(children);
        SocialWorker socialWorker = new SocialWorker();
        socialWorker.setLocalAuthorityEmail(TEST_USER_EMAIL);
        data.setChildSocialWorker(socialWorker);
        data.setApplicantSocialWorker(socialWorker);
        emailTemplatesConfig.getTemplateVars().put(LA_PORTAL_URL, TEST_LA_PORTAL_URL);
        Map<String, Object> templateVars = new HashMap<>();
        templateVars.put(HYPHENATED_REF, data.getHyphenatedCaseRef());
        templateVars.put(CHILD_FULL_NAME, data.getChildren().getFirstName() + " " + data.getChildren().getLastName());
        templateVars.put(LA_PORTAL_URL, emailTemplatesConfig.getTemplateVars().get(LA_PORTAL_URL));

        notification.sendToLocalAuthorityPostLocalAuthoritySubmission(data, 1234567890123456L);

        verify(notificationService, times(2)).sendEmail(
            TEST_USER_EMAIL,
            LOCAL_AUTHORITY_APPLICATION_SUBMITTED,
            templateVars,
            ENGLISH
        );
    }

    @Test
    void shouldSendEmailToApplicantsPostLocalAuthoritySubmissionWithSubmissionResponseDate() {
        CaseData caseData = caseData();
        caseData.setDueDate(LocalDate.of(2021, 4, 21));
        when(commonContent.mainTemplateVars(caseData, 1234567890123456L, caseData.getApplicant1(), caseData.getApplicant2()))
            .thenReturn(getMainTemplateVars());
        caseData.setFamilyCourtName(StringUtils.EMPTY);
        Map<String, Object> templateVars = new HashMap<>();
        templateVars.put(HYPHENATED_REF, caseData.getHyphenatedCaseRef());
        templateVars.put(SUBMISSION_RESPONSE_DATE, "21 April 2021");
        templateVars.put(APPLICATION_REFERENCE, "1234-5678-9012-3456");
        templateVars.put(APPLICANT_1_FULL_NAME, caseData.getApplicant1().getFirstName() + " "
            + caseData.getApplicant1().getLastName());
        templateVars.put(LOCAL_COURT_NAME, caseData.getFamilyCourtName());
        if (caseData.getApplicant2() != null) {
            templateVars.put(
                APPLICANT_2_FULL_NAME,
                caseData.getApplicant2().getFirstName() + " " + caseData.getApplicant2().getLastName()
            );
            templateVars.put(HAS_SECOND_APPLICANT, YES);
        } else {
            templateVars.put(HAS_SECOND_APPLICANT, NO);
            templateVars.put(APPLICANT_2_FULL_NAME, StringUtils.EMPTY);
        }
        templateVars.put(ADOPTION_CUI_MULTI_CHILDREN_URL, emailTemplatesConfig.getTemplateVars().get(ADOPTION_CUI_MULTI_CHILDREN_URL));

        notification.sendToApplicantsPostLocalAuthoritySubmission(caseData, 1234567890123456L);

        verify(notificationService, times(2)).sendEmail(
            TEST_USER_EMAIL,
            LOCAL_AUTHORITY_APPLICATION_SUBMITTED_ACKNOWLEDGE_CITIZEN,
            templateVars,
            ENGLISH
        );
        verify(commonContent).mainTemplateVars(caseData, 1234567890123456L, caseData.getApplicant1(), caseData.getApplicant2());
    }

    @Test
    void shouldSendEmailToLocalCourt() throws NotificationClientException, IOException {
        CaseData data = caseData();
        data.setHyphenatedCaseRef("1234-1234-1234-1234");
        AdoptionDocument adoptionDocument = AdoptionDocument.builder().documentType(DocumentType.APPLICATION_LA_SUMMARY_EN)
            .documentLink(Document.builder().url("/123/123e4567-e89b-42d3-a456-556642440000")
                    .build()).documentFileId("123e4567-e89b-42d3-a456-556642440000").build();
        ListValue<AdoptionDocument> listValue = new ListValue<>();
        listValue.setValue(adoptionDocument);
        List<ListValue<AdoptionDocument>> listOfUploadedDocument = List.of(listValue);
        data.setLaDocumentsUploaded(listOfUploadedDocument);
        data.setDocumentsGenerated(listOfUploadedDocument);
        data.setFamilyCourtEmailId(TEST_USER_EMAIL);
        data.setDueDate(LocalDate.of(2021, 4, 21));
        Children children = new Children();
        children.setFirstName("MOCK_FIRST_NAME");
        children.setLastName("MOCK_LAST_NAME");
        data.setChildren(children);

        ResponseEntity<Resource> resource = new ResponseEntity<Resource>(
            new ByteArrayResource(new byte[]{}), HttpStatus.OK);
        notification.sendToLocalCourtPostLocalAuthoritySubmission(data, 1234567890123456L);

        verify(notificationService).sendEmail(any(), any(), any(), any());
    }

}
