package uk.gov.hmcts.reform.adoption.adoptioncase.common;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MessageSendDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionUploadDocument;
import uk.gov.hmcts.reform.idam.client.models.User;
import uk.gov.hmcts.reform.idam.client.models.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.adoption.adoptioncase.common.CaseDataUtils.archiveListHelper;
import static uk.gov.hmcts.reform.adoption.adoptioncase.common.CaseEventCommonMethods.prepareReplyMessageDynamicList;
import static uk.gov.hmcts.reform.adoption.adoptioncase.common.CommonPageBuilder.sendMessageMidEvent;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_AUTHORIZATION_TOKEN;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

class CommonPageBuilderTest {

    @Test
    void sendMessageMidEvent_OK() {
        var caseData = getCaseDetails();
        List<ListValue<AdoptionUploadDocument>> applicationDocumentCategory = new ArrayList<>();
        caseData.getData().setApplicationDocumentsCategory(archiveListHelper(applicationDocumentCategory,
                                                                                    getApplicationDocumentCategory()));

        List<ListValue<AdoptionUploadDocument>> correspondenceDocCategory = new ArrayList<>();
        caseData.getData().setCorrespondenceDocumentCategory(archiveListHelper(correspondenceDocCategory,
                                                                                      getCorrespondanceDocumentCategory()));

        List<ListValue<AdoptionUploadDocument>> reportsDocumentCategory = new ArrayList<>();
        caseData.getData().setReportsDocumentCategory(archiveListHelper(reportsDocumentCategory,
                                                                               getReportsDocumentCategory()));

        List<ListValue<AdoptionUploadDocument>> statementDocumentCategory = new ArrayList<>();
        caseData.getData().setStatementsDocumentCategory(archiveListHelper(statementDocumentCategory,
                                                                                  getStatementsDocumentCategory()));

        List<ListValue<AdoptionUploadDocument>> courtOrderCategory = new ArrayList<>();
        caseData.getData().setCourtOrdersDocumentCategory(archiveListHelper(courtOrderCategory,
                                                                                   getCourtOrdersDocumentCategory()));

        List<ListValue<AdoptionUploadDocument>> additionalDocumentCategory = new ArrayList<>();
        caseData.getData().setAdditionalDocumentsCategory(archiveListHelper(additionalDocumentCategory,
                                                                                   getAdditionalDocumentsCategory()));

        List<ListValue<MessageSendDetails>> listOfOpenMessage = new ArrayList<>();
        var uuid = UUID.randomUUID();
        caseData.getData().setListOfOpenMessages(archiveListHelper(listOfOpenMessage,
                                                                          getListOfOpenMessages(uuid)));
        prepareReplyMessageDynamicList(caseData.getData(), getCaseworkerUser());
        caseData.getData().getReplyMsgDynamicList().setValue(new DynamicListElement(uuid, "test"));
        AboutToStartOrSubmitResponse<CaseData, State> response = sendMessageMidEvent(caseData, caseData);
        assertThat(response.getData().getSelectedMessage()).isNotNull();
    }

    private CaseDetails<CaseData, State> getCaseDetails() {
        final var details = new CaseDetails<CaseData, State>();
        final var data = caseData();
        details.setData(data);
        details.setId(1L);
        return details;
    }

    private MessageSendDetails getListOfOpenMessages(UUID uuid) {
        var messageSendDetails = new MessageSendDetails();
        messageSendDetails.setMessageId(uuid.toString());
        messageSendDetails.setMessageSendDateNTime(LocalDateTime.now());
        messageSendDetails.setMessageStatus(MessageSendDetails.MessageStatus.OPEN);
        messageSendDetails.setMessageReasonList(MessageSendDetails.MessageReason.ANNEX_A);
        messageSendDetails.setMessageUrgencyList(MessageSendDetails.MessageUrgency.HIGH);
        messageSendDetails.setMessageText("test");
        return  messageSendDetails;
    }

    private AdoptionUploadDocument getApplicationDocumentCategory() {
        var uploadDocument = new AdoptionUploadDocument();
        uploadDocument.setName("testdoc.jpg");
        uploadDocument.setDocumentLink(new Document());
        return  uploadDocument;
    }

    private AdoptionUploadDocument getReportsDocumentCategory() {
        var uploadDocument = new AdoptionUploadDocument();
        uploadDocument.setName("testdoc1.jpg");
        uploadDocument.setDocumentLink(new Document());
        return  uploadDocument;
    }


    private AdoptionUploadDocument getStatementsDocumentCategory() {
        var uploadDocument = new AdoptionUploadDocument();
        uploadDocument.setName("testdoc2.jpg");
        uploadDocument.setDocumentLink(new Document());
        return  uploadDocument;
    }


    private AdoptionUploadDocument getCourtOrdersDocumentCategory() {
        var uploadDocument = new AdoptionUploadDocument();
        uploadDocument.setName("testdoc3.jpg");
        uploadDocument.setDocumentLink(new Document());
        return  uploadDocument;
    }


    private AdoptionUploadDocument getCorrespondanceDocumentCategory() {
        var uploadDocument = new AdoptionUploadDocument();
        uploadDocument.setName("testdoc4.jpg");
        uploadDocument.setDocumentLink(new Document());
        return  uploadDocument;
    }

    private AdoptionUploadDocument getAdditionalDocumentsCategory() {
        var uploadDocument = new AdoptionUploadDocument();
        uploadDocument.setName("testdoc5.jpg");
        uploadDocument.setDocumentLink(new Document());
        return  uploadDocument;
    }

    private User getCaseworkerUser() {
        UserDetails userDetails = UserDetails
            .builder()
            .forename("testFname")
            .roles(Arrays.asList(UserRole.DISTRICT_JUDGE.getRole()))
            .surname("testSname")
            .build();

        return new User(TEST_AUTHORIZATION_TOKEN, userDetails);
    }
}
