package uk.gov.hmcts.reform.adoption.adoptioncase.common;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MessageDocumentList;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MessageSendDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.idam.client.models.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.SEND_N_REPLY_USER_DEFAULT;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.SEND_N_REPLY_USER_JUDGE;

public final class CaseEventCommonMethods {
    private CaseEventCommonMethods() {

    }

    public static List<MessageDocumentList> prepareDocumentList(CaseData caseData) {
        List<MessageDocumentList> messageDocumentLists = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(caseData.getAdditionalDocumentsCategory())) {
            caseData.getAdditionalDocumentsCategory().forEach(item -> {
                UUID result = UUID.nameUUIDFromBytes(item.getValue().getName().getBytes());
                messageDocumentLists.add(new MessageDocumentList(result.toString(), item.getValue().getDocumentLink()));
            });
        }

        if (CollectionUtils.isNotEmpty(caseData.getCorrespondenceDocumentCategory())) {
            caseData.getCorrespondenceDocumentCategory().forEach(item -> {
                if (item.getValue().getName() != null) {
                    UUID result = UUID.nameUUIDFromBytes(item.getValue().getName().getBytes());
                    messageDocumentLists.add(new MessageDocumentList(result.toString(), item.getValue().getDocumentLink()));
                }
            });
        }

        if (CollectionUtils.isNotEmpty(caseData.getReportsDocumentCategory())) {
            caseData.getReportsDocumentCategory().forEach(item -> {
                UUID result = UUID.nameUUIDFromBytes(item.getValue().getName().getBytes());
                messageDocumentLists.add(new MessageDocumentList(result.toString(), item.getValue().getDocumentLink()));
            });
        }

        if (CollectionUtils.isNotEmpty(caseData.getStatementsDocumentCategory())) {
            caseData.getStatementsDocumentCategory().forEach(item -> {
                UUID result = UUID.nameUUIDFromBytes(item.getValue().getName().getBytes());
                messageDocumentLists.add(new MessageDocumentList(result.toString(), item.getValue().getDocumentLink()));
            });
        }

        if (CollectionUtils.isNotEmpty(caseData.getCourtOrdersDocumentCategory())) {
            caseData.getCourtOrdersDocumentCategory().forEach(item -> {
                UUID result = UUID.nameUUIDFromBytes(item.getValue().getName().getBytes());
                messageDocumentLists.add(new MessageDocumentList(result.toString(), item.getValue().getDocumentLink()));
            });
        }

        if (CollectionUtils.isNotEmpty(caseData.getApplicationDocumentsCategory())) {
            caseData.getApplicationDocumentsCategory().forEach(item -> {
                UUID result = UUID.nameUUIDFromBytes(item.getValue().getName().getBytes());
                messageDocumentLists.add(new MessageDocumentList(result.toString(), item.getValue().getDocumentLink()));
            });
        }
        return messageDocumentLists;
    }

    public static void prepareReplyMessageDynamicList(CaseData caseData, User caseworkerUser) {
        List<DynamicListElement> replyMessageList = new ArrayList<>();


        caseData.setLoggedInUserRole(caseworkerUser.getUserDetails().getRoles()
                                         .contains(UserRole.DISTRICT_JUDGE.getRole())
                                         ? SEND_N_REPLY_USER_JUDGE : SEND_N_REPLY_USER_DEFAULT);
    }

    public static String getMessageReasonLabel(MessageSendDetails item) {
        if (item.getMessageReasonList() != null && item.getMessageReasonList().getLabel() != null) {
            return item.getMessageReasonList().getLabel();
        } else if (item.getMessageReasonJudge() != null && item.getMessageReasonJudge().getLabel() != null) {
            return item.getMessageReasonJudge().getLabel();
        }
        return null;
    }

    public static void updateMessageList(CaseData caseData, User caseworkerUser) {


        caseData.setLoggedInUserRole(null);
    }

    private static ListValue<MessageSendDetails> getSelectedMessage(CaseData caseData, String activeMessageID) {

        return null;
    }

    private static void setMessageInformation(CaseData caseData, MessageSendDetails sendMessagesDetails, User caseworkerUser) {
        sendMessagesDetails.setMessageFrom(caseworkerUser.getUserDetails().getEmail());
        sendMessagesDetails.setMessageStatus(MessageSendDetails.MessageStatus.OPEN);
        sendMessagesDetails.setMessageSendDateNTime(
            LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
    }

    public static String buildMessageHistory(User caseworkerUser, String messageText, String messageHistory) {
        String newMessage = String.format(
            "%s %s %s",
            caseworkerUser.getUserDetails().getEmail(),
            " - ",
            messageText);
        if (StringUtils.isEmpty(messageHistory)) {
            return newMessage;
        }
        return String.join("\n \n", newMessage, messageHistory);
    }

    private static void buildDocumentHistory(CaseData caseData, MessageSendDetails sendMessagesDetails,
                                      List<ListValue<Document>> documentHistory) {

    }
}
