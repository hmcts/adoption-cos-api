package uk.gov.hmcts.reform.adoption.adoptioncase.common;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MessageDocumentList;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MessageSendDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.idam.client.models.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.SEND_N_REPLY_USER_DEFAULT;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.SEND_N_REPLY_USER_JUDGE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.SEND_N_REPLY_DATE_FORMAT;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.COMMA;


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
        if (CollectionUtils.isNotEmpty(caseData.getListOfOpenMessages())) {
            caseData.getListOfOpenMessages().forEach(item -> {
                DynamicListElement orderInfo = DynamicListElement.builder()
                    .label(item.getValue().getMessageSendDateNTime().format(
                            DateTimeFormatter.ofPattern(
                                SEND_N_REPLY_DATE_FORMAT)).concat(COMMA)
                               .concat(getMessageReasonLabel(item.getValue()))).code(
                        UUID.fromString(item.getValue().getMessageId())).build();
                replyMessageList.add(orderInfo);
            });

        }
        caseData.setReplyMsgDynamicList(DynamicList.builder().listItems(replyMessageList)
                                            .value(DynamicListElement.EMPTY).build());
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
        if (caseData.getMessageAction().equals(MessageSendDetails.MessagesAction.SEND_A_MESSAGE)) {
            MessageSendDetails sendMessagesDetails = caseData.getMessageSendDetails();
            sendMessagesDetails.setMessageId(UUID.randomUUID().toString());
            sendMessagesDetails.setMessageHistory(buildMessageHistory(
                caseworkerUser,
                sendMessagesDetails.getMessageText(),
                sendMessagesDetails.getMessageHistory()
            ));
            buildDocumentHistory(caseData, sendMessagesDetails,sendMessagesDetails.getDocumentHistory());
            setMessageInformation(caseData, sendMessagesDetails,caseworkerUser);

        } else if (caseData.getMessageAction().equals(MessageSendDetails.MessagesAction.REPLY_A_MESSAGE)) {
            MessageSendDetails sendMessagesDetails = caseData.getMessageSendDetails();
            String activeMessageID = caseData.getReplyMsgDynamicList().getValueCode().toString();
            ListValue<MessageSendDetails> messageListValue = getSelectedMessage(caseData, activeMessageID);
            if (Objects.nonNull(messageListValue)) {
                MessageSendDetails selectedMessage = messageListValue.getValue();
                caseData.getListOfOpenMessages().remove(messageListValue);
                if (YesOrNo.NO.equals(caseData.getSelectedMessage().getReplyMessage())) {
                    selectedMessage.setMessageStatus(MessageSendDetails.MessageStatus.CLOSED);
                    caseData.setClosedMessages(caseData.archiveManageOrdersHelper(
                        caseData.getClosedMessages(), selectedMessage));
                } else {
                    sendMessagesDetails.setMessageId(activeMessageID);
                    sendMessagesDetails.setMessageHistory(
                        buildMessageHistory(caseworkerUser, sendMessagesDetails.getMessageText(), selectedMessage.getMessageHistory()));
                    buildDocumentHistory(caseData, sendMessagesDetails, selectedMessage.getDocumentHistory());
                    setMessageInformation(caseData, sendMessagesDetails,caseworkerUser);
                }
            }
        }
        caseData.setMessageAction(null);
        caseData.setLoggedInUserRole(null);
    }

    private static ListValue<MessageSendDetails> getSelectedMessage(CaseData caseData, String activeMessageID) {
        Optional<ListValue<MessageSendDetails>> selectedMessage = caseData.getListOfOpenMessages().stream()
            .filter(item -> item.getValue().getMessageId().equalsIgnoreCase(
            activeMessageID)).findFirst();
        if (selectedMessage.isPresent()) {
            return selectedMessage.get();
        }
        return null;
    }

    private static void setMessageInformation(CaseData caseData, MessageSendDetails sendMessagesDetails, User caseworkerUser) {
        sendMessagesDetails.setMessageFrom(caseworkerUser.getUserDetails().getEmail());
        sendMessagesDetails.setMessageStatus(MessageSendDetails.MessageStatus.OPEN);
        sendMessagesDetails.setMessageSendDateNTime(
            LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
        caseData.setListOfOpenMessages(caseData.archiveManageOrdersHelper(
            caseData.getListOfOpenMessages(), sendMessagesDetails));
        caseData.setMessageSendDetails(null);
        caseData.setAttachDocumentList(null);
        caseData.setSelectedMessage(null);
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
        if (caseData.getAttachDocumentList() != null
            && caseData.getAttachDocumentList().getValue() != null) {

            Optional<MessageDocumentList> selectedDocument = CaseEventCommonMethods.prepareDocumentList(caseData).stream()
                .filter(item -> item.getMessageId().equalsIgnoreCase(caseData.getAttachDocumentList().getValue().getCode().toString()))
                .findFirst();
            if (selectedDocument.isPresent()) {
                var doc = selectedDocument.get().getDocumentLink();
                sendMessagesDetails.setSelectedDocument(doc);
                sendMessagesDetails.setDocumentHistory(
                    caseData.archiveManageOrdersHelper(documentHistory, doc));
            }

        }

    }
}
