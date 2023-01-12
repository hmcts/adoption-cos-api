package uk.gov.hmcts.reform.adoption.adoptioncase.common;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MessageSendDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MessageDocumentList;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.SelectedMessage;
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
import static uk.gov.hmcts.reform.adoption.adoptioncase.common.CaseEventCommonMethods.prepareDocumentList;
import static uk.gov.hmcts.reform.adoption.adoptioncase.common.CaseEventCommonMethods.prepareReplyMessageDynamicList;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_AUTHORIZATION_TOKEN;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

public class CaseEventCommonMethodsTest {

    @Test
    public void verifyMessageDocumentList_OK() {
        var caseData = getCaseDetails().getData();
        List<ListValue<AdoptionUploadDocument>> applicationDocumentCategory = new ArrayList<>();
        caseData.setApplicationDocumentsCategory(archiveListHelper(
            applicationDocumentCategory, getApplicationDocumentCategory()));

        List<ListValue<AdoptionUploadDocument>> correspondenceDocCategory = new ArrayList<>();
        caseData.setCorrespondenceDocumentCategory(archiveListHelper(
            correspondenceDocCategory, getCorrespondanceDocumentCategory()));

        List<ListValue<AdoptionUploadDocument>> reportsDocumentCategory = new ArrayList<>();
        caseData.setReportsDocumentCategory(archiveListHelper(
            reportsDocumentCategory, getReportsDocumentCategory()));

        List<ListValue<AdoptionUploadDocument>> statementDocumentCategory = new ArrayList<>();
        caseData.setStatementsDocumentCategory(archiveListHelper(
            statementDocumentCategory, getStatementsDocumentCategory()));

        List<ListValue<AdoptionUploadDocument>> courtOrderCategory = new ArrayList<>();
        caseData.setCourtOrdersDocumentCategory(archiveListHelper(
            courtOrderCategory, getCourtOrdersDocumentCategory()));

        List<ListValue<AdoptionUploadDocument>> additionalDocumentCategory = new ArrayList<>();
        caseData.setAdditionalDocumentsCategory(archiveListHelper(
            additionalDocumentCategory, getAdditionalDocumentsCategory()));

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
        prepareReplyMessageDynamicList(caseData, getCaseworkerUser());
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
        messageSendDetails.setMessageReasonList(MessageSendDetails.MessageReason.ANNEX_A);
        caseData.setMessageSendDetails(messageSendDetails);
        CaseEventCommonMethods.updateMessageList(caseData, getCaseworkerUser());
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
        messageSendDetails.setMessageReasonList(MessageSendDetails.MessageReason.ANNEX_A);
        caseData.setMessageSendDetails(messageSendDetails);
        List<ListValue<MessageSendDetails>> listOfOpenMessage = new ArrayList<>();
        caseData.setListOfOpenMessages(caseData.archiveManageOrdersHelper(listOfOpenMessage,
                                                                          getListOfOpenMessages(uuid)));
        var selectedMessage = new SelectedMessage();
        selectedMessage.setReplyMessage(YesOrNo.YES);
        caseData.setSelectedMessage(selectedMessage);
        prepareReplyMessageDynamicList(caseData, getCaseworkerUser());
        caseData.getReplyMsgDynamicList().setValue(new DynamicListElement(uuid, "Test"));
        CaseEventCommonMethods.updateMessageList(caseData, getCaseworkerUser());
        assertThat(caseData.getMessageAction()).isNull();
    }

    private MessageSendDetails getListOfOpenMessages(UUID uuid) {
        var messageSendDetails = new MessageSendDetails();
        messageSendDetails.setMessageId(uuid.toString());
        messageSendDetails.setMessageSendDateNTime(LocalDateTime.now());
        messageSendDetails.setMessageStatus(MessageSendDetails.MessageStatus.OPEN);
        messageSendDetails.setMessageReasonList(MessageSendDetails.MessageReason.ANNEX_A);
        return  messageSendDetails;
    }


    @Test
    public void verifyMessageDocumentList_recordNotFound() {
        List<MessageDocumentList> list = prepareDocumentList(getCaseDetails().getData());
        assertThat(list).hasSize(0);
        assertThat(list).isEmpty();
    }

    @Test
    public void verifyDocumentHistory_Test() {
        var caseData = getCaseDetails().getData();
        var uploadDocument = getApplicationDocumentCategory();
        var uuid = UUID.nameUUIDFromBytes(uploadDocument.getName().getBytes());
        var dynamicList = new DynamicList();
        dynamicList.setValue(new DynamicListElement(uuid, "test"));
        caseData.setAttachDocumentList(dynamicList);
        List<ListValue<AdoptionUploadDocument>> applicationDocumentCategory = new ArrayList<>();
        caseData.setApplicationDocumentsCategory(caseData.archiveManageOrdersHelper(applicationDocumentCategory,
                                                                                    uploadDocument));
        prepareDocumentList(caseData);

        var messageSendDetails = new MessageSendDetails();
        caseData.setMessageAction(MessageSendDetails.MessagesAction.SEND_A_MESSAGE);
        messageSendDetails.setMessageId("123e4567-e89b-12d3-a456-426614174000");
        messageSendDetails.setMessageSendDateNTime(LocalDateTime.now());
        messageSendDetails.setMessageStatus(MessageSendDetails.MessageStatus.OPEN);
        messageSendDetails.setMessageReasonList(MessageSendDetails.MessageReason.ANNEX_A);
        caseData.setMessageSendDetails(messageSendDetails);
        CaseEventCommonMethods.updateMessageList(caseData, getCaseworkerUser());

        assertThat(messageSendDetails.getDocumentHistory()).isNotNull();
        assertThat(messageSendDetails.getSelectedDocument()).isNotNull();
    }

    @Test
    public void verifyMessageReasonLabel_Test_ReasonList() {
        var messageSendDetails = new MessageSendDetails();
        var caseData = getCaseDetails().getData();
        messageSendDetails.setMessageReasonList(MessageSendDetails.MessageReason.ANNEX_A);
        caseData.setMessageSendDetails(messageSendDetails);
        assertThat(CaseEventCommonMethods.getMessageReasonLabel(messageSendDetails)).isEqualTo("Annex A for review");

    }

    @Test
    public void verifyMessageReasonLabel_Test_ReasonJudge() {
        var messageSendDetails = new MessageSendDetails();
        var caseData = getCaseDetails().getData();
        messageSendDetails.setMessageReasonJudge(MessageSendDetails.MessageReasonJudge.LIST_A_HEARING);
        caseData.setMessageSendDetails(messageSendDetails);
        assertThat(CaseEventCommonMethods.getMessageReasonLabel(messageSendDetails)).isEqualTo("List for a hearing");
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

    private User getCaseworkerUser() {
        UserDetails userDetails = UserDetails
            .builder()
            .roles(Arrays.asList(UserRole.DISTRICT_JUDGE.getRole()))
            .forename("testFname")
            .surname("testSname")
            .build();

        return new User(TEST_AUTHORIZATION_TOKEN, userDetails);
    }
}
