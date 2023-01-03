package uk.gov.hmcts.reform.adoption.adoptioncase.common;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MessageDocumentList;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MessageSendDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.SelectedMessage;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionUploadDocument;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.adoption.adoptioncase.common.CaseEventCommonMethods.prepareDocumentList;
import static uk.gov.hmcts.reform.adoption.adoptioncase.common.CaseEventCommonMethods.prepareReplyMessageDynamicList;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

public class CaseEventCommonMethodsTest {



    @Test
    public void verifyMessageDocumentList_OK() {
        var caseData = getCaseDetails().getData();
        List<ListValue<AdoptionUploadDocument>> applicationDocumentCategory = new ArrayList<>();
        caseData.setApplicationDocumentsCategory(caseData.archiveManageOrdersHelper(applicationDocumentCategory,
                                                                                    getApplicationDocumentCategory()));

        List<ListValue<AdoptionUploadDocument>> correspondenceDocCategory = new ArrayList<>();
        caseData.setCorrespondenceDocumentCategory(caseData.archiveManageOrdersHelper(correspondenceDocCategory,
                                                                                      getCorrespondanceDocumentCategory()));

        List<ListValue<AdoptionUploadDocument>> reportsDocumentCategory = new ArrayList<>();
        caseData.setReportsDocumentCategory(caseData.archiveManageOrdersHelper(reportsDocumentCategory,
                                                                                      getReportsDocumentCategory()));

        List<ListValue<AdoptionUploadDocument>> statementDocumentCategory = new ArrayList<>();
        caseData.setStatementsDocumentCategory(caseData.archiveManageOrdersHelper(statementDocumentCategory,
                                                                                      getStatementsDocumentCategory()));

        List<ListValue<AdoptionUploadDocument>> courtOrderCategory = new ArrayList<>();
        caseData.setCourtOrdersDocumentCategory(caseData.archiveManageOrdersHelper(courtOrderCategory,
                                                                                      getCourtOrdersDocumentCategory()));

        List<ListValue<AdoptionUploadDocument>> additionalDocumentCategory = new ArrayList<>();
        caseData.setAdditionalDocumentsCategory(caseData.archiveManageOrdersHelper(additionalDocumentCategory,
                                                                                      getAdditionalDocumentsCategory()));

        List<MessageDocumentList> list = prepareDocumentList(caseData);
        assertThat(list).hasSize(6);
        assertThat(list).isNotEmpty();
    }

    @Test
    public void prepareReplyMessageList_OK() {
        var caseData = getCaseDetails().getData();
        List<ListValue<MessageSendDetails>> listOfOpenMessage = new ArrayList<>();
        caseData.setListOfOpenMessages(caseData.archiveManageOrdersHelper(listOfOpenMessage,
                                                                          getListOfOpenMessages(UUID.randomUUID())));
        prepareReplyMessageDynamicList(caseData);
        assertThat(caseData.getReplyMsgDynamicList()).isNotNull();
    }

    @Test
    public void updateMessageList_SendMessage_Test_OK() {
        var caseData = getCaseDetails().getData();
        caseData.setMessageAction(MessageSendDetails.MessagesAction.SEND_A_MESSAGE);
        var messageSendDetails = new MessageSendDetails();
        messageSendDetails.setMessageId("123e4567-e89b-12d3-a456-426614174000");
        messageSendDetails.setMessageSendDateNTime(LocalDateTime.now());
        messageSendDetails.setMessageStatus(MessageSendDetails.MessageStatus.OPEN);
        messageSendDetails.setMessageReasonList(MessageSendDetails.MessageReason.LIST_A_HEARING);
        caseData.setMessageSendDetails(messageSendDetails);
        CaseEventCommonMethods.updateMessageList(caseData);
        assertThat(caseData.getMessageAction()).isNull();
    }

    @Test
    public void updateMessageList_ReplyMessage_Test_OK() {
        var caseData = getCaseDetails().getData();
        caseData.setMessageAction(MessageSendDetails.MessagesAction.REPLY_A_MESSAGE);
        var messageSendDetails = new MessageSendDetails();
        var uuid = UUID.randomUUID();
        messageSendDetails.setMessageId(uuid.toString());
        messageSendDetails.setMessageSendDateNTime(LocalDateTime.now());
        messageSendDetails.setMessageStatus(MessageSendDetails.MessageStatus.CLOSED);
        messageSendDetails.setMessageReasonList(MessageSendDetails.MessageReason.LIST_A_HEARING);
        caseData.setMessageSendDetails(messageSendDetails);
        List<ListValue<MessageSendDetails>> listOfOpenMessage = new ArrayList<>();
        caseData.setListOfOpenMessages(caseData.archiveManageOrdersHelper(listOfOpenMessage,
                                                                          getListOfOpenMessages(uuid)));
        var selectedMessage = new SelectedMessage();
        selectedMessage.setReplyMessage(YesOrNo.YES);
        caseData.setSelectedMessage(selectedMessage);
        prepareReplyMessageDynamicList(caseData);
        caseData.getReplyMsgDynamicList().setValue(new DynamicListElement(uuid, "Test"));
        CaseEventCommonMethods.updateMessageList(caseData);
        assertThat(caseData.getMessageAction()).isNull();
    }

    private MessageSendDetails getListOfOpenMessages(UUID uuid) {
        var messageSendDetails = new MessageSendDetails();
        messageSendDetails.setMessageId(uuid.toString());
        messageSendDetails.setMessageSendDateNTime(LocalDateTime.now());
        messageSendDetails.setMessageStatus(MessageSendDetails.MessageStatus.OPEN);
        messageSendDetails.setMessageReasonList(MessageSendDetails.MessageReason.LIST_A_HEARING);
        return  messageSendDetails;
    }


    @Test
    public void verifyMessageDocumentList_recordNotFound() {
        List<MessageDocumentList> list = prepareDocumentList(getCaseDetails().getData());
        assertThat(list).hasSize(0);
        assertThat(list).isEmpty();
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

    private CaseDetails<CaseData, State> getCaseDetails() {
        final var details = new CaseDetails<CaseData, State>();
        final var data = caseData();
        details.setData(data);
        details.setId(1L);
        return details;
    }
}
