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
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.document.DocumentManagementClient;
import uk.gov.hmcts.reform.adoption.document.DocumentType;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionDocument;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.idam.client.models.User;
import uk.gov.hmcts.reform.idam.client.models.UserDetails;
import uk.gov.service.notify.NotificationClientException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference.ENGLISH;
import static uk.gov.hmcts.reform.adoption.notification.CommonContent.APPLICATION_REFERENCE;
import static uk.gov.hmcts.reform.adoption.notification.CommonContent.SUBMISSION_RESPONSE_DATE;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.APPLICANT_APPLICATION_SUBMITTED;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_USER_EMAIL;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.getMainTemplateVars;

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
    private DocumentManagementClient dmClient;

    @InjectMocks
    private ApplicationSubmittedNotification notification;

    @Test
    void shouldSendEmailToApplicantsWithSubmissionResponseDate() {
        CaseData data = caseData();
        data.setDueDate(LocalDate.of(2021, 4, 21));
        when(commonContent.mainTemplateVars(data, 1234567890123456L, data.getApplicant1(), data.getApplicant2()))
            .thenReturn(getMainTemplateVars());

        notification.sendToApplicants(data, 1234567890123456L);
        Map<String, Object> templateVars = new HashMap<>();
        templateVars.put(APPLICATION_REFERENCE, "1234-5678-9012-3456");
        templateVars.put(SUBMISSION_RESPONSE_DATE, "21 April 2021");

        verify(notificationService, times(2)).sendEmail(
            eq(TEST_USER_EMAIL),
            eq(APPLICANT_APPLICATION_SUBMITTED),
            eq(templateVars),
            eq(ENGLISH)
        );
        verify(commonContent).mainTemplateVars(data, 1234567890123456L, data.getApplicant1(), data.getApplicant2());
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
    void shouldSendEmailToLocalAuthorityWithSubmissionResponseDate() {
        CaseData data = caseData();
        data.setDueDate(LocalDate.of(2021, 4, 21));
        when(commonContent.mainTemplateVars(data, 1234567890123456L, data.getApplicant1(), data.getApplicant2()))
            .thenReturn(getMainTemplateVars());

        notification.sendToLocalAuthority(data, 1234567890123456L);
        Object submissionResponseDate = new String("21 April 2021");
        Object applicationReference = new String("1234-5678-9012-3456");

        verify(notificationService).sendEmail(
            eq(TEST_USER_EMAIL),
            eq(APPLICANT_APPLICATION_SUBMITTED),
            argThat(allOf(
                hasEntry(SUBMISSION_RESPONSE_DATE, submissionResponseDate),
                hasEntry(APPLICATION_REFERENCE, applicationReference)
            )),
            eq(ENGLISH)
        );
        verify(commonContent).mainTemplateVars(data, 1234567890123456L, data.getApplicant1(), data.getApplicant2());
    }

    @Test
    void shouldSendEmailToLocalCourt() throws NotificationClientException, IOException {
        CaseData data = caseData();
        data.setHyphenatedCaseRef("1234-1234-1234-1234");
        AdoptionDocument adoptionDocument = AdoptionDocument.builder().documentType(DocumentType.APPLICATION_SUMMARY).build();
        ListValue<AdoptionDocument> listValue = new ListValue<>();
        listValue.setValue(adoptionDocument);
        List<ListValue<AdoptionDocument>> listOfUploadedDocument = List.of(listValue);
        data.setApplicant1DocumentsUploaded(listOfUploadedDocument);
        data.setDocumentsGenerated(listOfUploadedDocument);
        data.setFamilyCourtEmailId(TEST_USER_EMAIL);
        data.setDueDate(LocalDate.of(2021, 4, 21));
        ResponseEntity<Resource> resource = new ResponseEntity<Resource>(
            new ByteArrayResource(new byte[]{}), HttpStatus.OK);
        when(idamService.retrieveSystemUpdateUserDetails()).thenReturn(new User(StringUtils.EMPTY, UserDetails.builder().build()));
        when(authTokenGenerator.generate()).thenReturn(StringUtils.EMPTY);
        when(dmClient.downloadBinary(anyString(), anyString(), any(), any(), any())).thenReturn(resource);

        notification.sendToLocalCourt(data, 1234567890123456L);

        verify(notificationService).sendEmail(any(), any(), any(), any());
    }

}
