package uk.gov.hmcts.reform.adoption.adoptioncase.common;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MessageDocumentList;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MessageSendDetails;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.COMMA;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.SEND_N_REPLY_DATE_FORMAT;


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


    public static void prepareReplyMessageDynamicList(CaseData caseData) {
        List<DynamicListElement> replyMessageList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(caseData.getListOfOpenMessages())) {
            caseData.getListOfOpenMessages().forEach(item -> {
                DynamicListElement orderInfo = DynamicListElement.builder()
                    .label(item.getValue().getMessageSendDateNTime().format(
                            DateTimeFormatter.ofPattern(
                                SEND_N_REPLY_DATE_FORMAT)).concat(COMMA)
                               .concat(item.getValue().getMessageReasonList().getLabel())).code(
                        UUID.fromString(item.getValue().getMessageId())).build();
                replyMessageList.add(orderInfo);
            });

        }
        caseData.setReplyMsgDynamicList(DynamicList.builder().listItems(replyMessageList)
                                            .value(DynamicListElement.EMPTY).build());
    }

    public static void updateMessageList(CaseData caseData) {
        if (caseData.getMessageAction().equals(MessageSendDetails.MessagesAction.SEND_A_MESSAGE)) {
            MessageSendDetails sendMessagesDetails = caseData.getMessageSendDetails();
            sendMessagesDetails.setMessageId(UUID.randomUUID().toString());
            sendMessagesDetails.setMessageHistory(sendMessagesDetails.getMessageText());
            setMessageInformation(caseData, sendMessagesDetails);

        } else if (caseData.getMessageAction().equals(MessageSendDetails.MessagesAction.REPLY_A_MESSAGE)) {
            MessageSendDetails sendMessagesDetails = caseData.getMessageSendDetails();
            String activeMessageID = caseData.getReplyMsgDynamicList().getValueCode().toString();
            ListValue<MessageSendDetails> messageListValue = getSelectedMessage(caseData, activeMessageID);
            MessageSendDetails selectedMessage = messageListValue.getValue();
            caseData.getListOfOpenMessages().remove(messageListValue);
            if (YesOrNo.NO.equals(caseData.getSelectedMessage().getReplyMessage())) {
                caseData.setClosedMessages(caseData.archiveManageOrdersHelper(
                    caseData.getClosedMessages(), selectedMessage));

            } else {
                sendMessagesDetails.setMessageId(activeMessageID);
                sendMessagesDetails.setMessageHistory(
                    StringUtils.appendIfMissing(sendMessagesDetails.getMessageText(),
                                                "\n",
                                                selectedMessage.getMessageHistory()));
                CaseEventCommonMethods.setMessageInformation(caseData, sendMessagesDetails);
            }

        }
        caseData.setMessageAction(null);
    }

    private static ListValue<MessageSendDetails> getSelectedMessage(CaseData caseData, String activeMessageID) {
        return caseData.getListOfOpenMessages().stream().filter(item -> item.getValue().getMessageId().equalsIgnoreCase(
            activeMessageID)).findFirst().get();
    }

    private static void setMessageInformation(CaseData caseData, MessageSendDetails sendMessagesDetails) {
        if (caseData.getAttachDocumentList() != null
            && caseData.getAttachDocumentList().getValue() != null) {
            var doc = CaseEventCommonMethods.prepareDocumentList(caseData).stream()
                .filter(item -> item.getMessageId().equalsIgnoreCase(caseData.getAttachDocumentList().getValue().getCode().toString()))
                .findFirst().get().getDocumentLink();
            sendMessagesDetails.setSelectedDocument(doc);
            sendMessagesDetails.setDocumentHistory(
                caseData.archiveManageOrdersHelper(sendMessagesDetails.getDocumentHistory(), doc));
        }
        sendMessagesDetails.setMessageStatus(MessageSendDetails.MessageStatus.OPEN);
        sendMessagesDetails.setMessageSendDateNTime(
            LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
        caseData.setListOfOpenMessages(caseData.archiveManageOrdersHelper(
            caseData.getListOfOpenMessages(), sendMessagesDetails));
        caseData.setMessageSendDetails(null);
        caseData.setAttachDocumentList(null);
        caseData.setSelectedMessage(null);
    }
}
