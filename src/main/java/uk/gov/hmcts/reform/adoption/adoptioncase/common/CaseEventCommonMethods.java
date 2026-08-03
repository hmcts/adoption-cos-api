package uk.gov.hmcts.reform.adoption.adoptioncase.common;

import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MessageDocumentList;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MessageSendDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionUploadDocument;
import uk.gov.hmcts.reform.idam.client.models.User;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

import static uk.gov.hmcts.reform.adoption.adoptioncase.common.CaseDataUtils.archiveListHelper;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.COMMA;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.SEND_N_REPLY_DATE_FORMAT;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.SEND_N_REPLY_USER_DEFAULT;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.SEND_N_REPLY_USER_JUDGE;

public final class CaseEventCommonMethods {

    private CaseEventCommonMethods() {
    }

    public static List<MessageDocumentList> prepareDocumentList(CaseData caseData) {
        List<MessageDocumentList> messageDocumentLists = new ArrayList<>();

        addDocuments(messageDocumentLists, caseData.getAdditionalDocumentsCategory());
        addDocuments(messageDocumentLists, caseData.getCorrespondenceDocumentCategory());
        addDocuments(messageDocumentLists, caseData.getReportsDocumentCategory());
        addDocuments(messageDocumentLists, caseData.getStatementsDocumentCategory());
        addDocuments(messageDocumentLists, caseData.getCourtOrdersDocumentCategory());
        addDocuments(messageDocumentLists, caseData.getApplicationDocumentsCategory());

        return messageDocumentLists;
    }

    private static void addDocuments(
        List<MessageDocumentList> messageDocumentLists,
        List<ListValue<AdoptionUploadDocument>> documents
    ) {
        addDocuments(
            messageDocumentLists,
            documents,
            AdoptionUploadDocument::getName,
            AdoptionUploadDocument::getDocumentLink
        );
    }

    private static <T> void addDocuments(
        List<MessageDocumentList> messageDocumentLists,
        List<ListValue<T>> documents,
        Function<T, String> nameExtractor,
        Function<T, Document> documentLinkExtractor
    ) {
        emptyIfNull(documents).stream()
            .map(ListValue::getValue)
            .filter(Objects::nonNull)
            .filter(document -> nameExtractor.apply(document) != null)
            .map(document -> toMessageDocumentList(
                nameExtractor.apply(document),
                documentLinkExtractor.apply(document)
            ))
            .forEach(messageDocumentLists::add);
    }

    private static MessageDocumentList toMessageDocumentList(String name, Document documentLink) {
        UUID messageId = UUID.nameUUIDFromBytes(name.getBytes(StandardCharsets.UTF_8));
        return new MessageDocumentList(messageId.toString(), documentLink);
    }

    public static void prepareReplyMessageDynamicList(CaseData caseData, User caseworkerUser) {
        List<DynamicListElement> replyMessageList = emptyIfNull(caseData.getListOfOpenMessages()).stream()
            .map(CaseEventCommonMethods::toReplyMessageListElement)
            .toList();

        caseData.setReplyMsgDynamicList(DynamicList.builder()
                                            .listItems(replyMessageList)
                                            .value(DynamicListElement.EMPTY)
                                            .build());

        caseData.setLoggedInUserRole(isDistrictJudge(caseworkerUser)
                                         ? SEND_N_REPLY_USER_JUDGE
                                         : SEND_N_REPLY_USER_DEFAULT);
    }

    private static DynamicListElement toReplyMessageListElement(ListValue<MessageSendDetails> item) {
        MessageSendDetails message = item.getValue();
        String reasonLabel = getMessageReasonLabel(message);

        String label = message.getMessageSendDateNTime()
            .format(DateTimeFormatter.ofPattern(SEND_N_REPLY_DATE_FORMAT))
            .concat(COMMA)
            .concat(reasonLabel == null ? "" : reasonLabel);

        return DynamicListElement.builder()
            .label(label)
            .code(UUID.fromString(message.getMessageId()))
            .build();
    }

    private static boolean isDistrictJudge(User caseworkerUser) {
        return caseworkerUser.getUserDetails()
            .getRoles()
            .contains(UserRole.DISTRICT_JUDGE.getRole());
    }

    public static String getMessageReasonLabel(MessageSendDetails item) {
        if (item.getMessageReasonList() != null && item.getMessageReasonList().getLabel() != null) {
            return item.getMessageReasonList().getLabel();
        }

        if (item.getMessageReasonJudge() != null && item.getMessageReasonJudge().getLabel() != null) {
            return item.getMessageReasonJudge().getLabel();
        }

        return null;
    }

    public static void updateMessageList(CaseData caseData, User caseworkerUser) {
        if (MessageSendDetails.MessagesAction.SEND_A_MESSAGE.equals(caseData.getMessageAction())) {
            MessageSendDetails sendMessagesDetails = caseData.getMessageSendDetails();

            sendMessagesDetails.setMessageId(UUID.randomUUID().toString());
            sendMessagesDetails.setMessageHistory(buildMessageHistory(
                caseworkerUser,
                sendMessagesDetails.getMessageText(),
                sendMessagesDetails.getMessageHistory()
            ));

            buildDocumentHistory(caseData, sendMessagesDetails, sendMessagesDetails.getDocumentHistory());
            setMessageInformation(caseData, sendMessagesDetails, caseworkerUser);

        } else if (MessageSendDetails.MessagesAction.REPLY_A_MESSAGE.equals(caseData.getMessageAction())) {
            MessageSendDetails sendMessagesDetails = caseData.getMessageSendDetails();
            String activeMessageId = caseData.getReplyMsgDynamicList().getValueCode().toString();

            ListValue<MessageSendDetails> messageListValue = getSelectedMessage(caseData, activeMessageId);

            if (Objects.nonNull(messageListValue)) {
                MessageSendDetails selectedMessage = messageListValue.getValue();
                caseData.getListOfOpenMessages().remove(messageListValue);

                if (YesOrNo.NO.equals(caseData.getSelectedMessage().getReplyMessage())) {
                    selectedMessage.setMessageStatus(MessageSendDetails.MessageStatus.CLOSED);
                    caseData.setClosedMessages(archiveListHelper(
                        caseData.getClosedMessages(),
                        selectedMessage
                    ));
                } else {
                    sendMessagesDetails.setMessageId(activeMessageId);
                    sendMessagesDetails.setMessageHistory(buildMessageHistory(
                        caseworkerUser,
                        sendMessagesDetails.getMessageText(),
                        selectedMessage.getMessageHistory()
                    ));

                    buildDocumentHistory(caseData, sendMessagesDetails, selectedMessage.getDocumentHistory());
                    setMessageInformation(caseData, sendMessagesDetails, caseworkerUser);
                }
            }
        }

        caseData.setMessageAction(null);
        caseData.setLoggedInUserRole(null);
    }

    private static ListValue<MessageSendDetails> getSelectedMessage(CaseData caseData, String activeMessageId) {
        return emptyIfNull(caseData.getListOfOpenMessages()).stream()
            .filter(item -> item.getValue().getMessageId().equalsIgnoreCase(activeMessageId))
            .findFirst()
            .orElse(null);
    }

    private static void setMessageInformation(
        CaseData caseData,
        MessageSendDetails sendMessagesDetails,
        User caseworkerUser
    ) {
        sendMessagesDetails.setMessageFrom(caseworkerUser.getUserDetails().getEmail());
        sendMessagesDetails.setMessageStatus(MessageSendDetails.MessageStatus.OPEN);
        sendMessagesDetails.setMessageSendDateNTime(LocalDateTime.ofInstant(
            Instant.now(),
            ZoneId.systemDefault()
        ));

        caseData.setListOfOpenMessages(archiveListHelper(
            caseData.getListOfOpenMessages(),
            sendMessagesDetails
        ));

        caseData.setMessageSendDetails(null);
        caseData.setAttachDocumentList(null);
        caseData.setSelectedMessage(null);
    }

    public static String buildMessageHistory(User caseworkerUser, String messageText, String messageHistory) {
        String newMessage = String.format(
            "%s - %s",
            caseworkerUser.getUserDetails().getEmail(),
            messageText
        );

        if (messageHistory == null || messageHistory.isEmpty()) {
            return newMessage;
        }

        return String.join("\n \n", newMessage, messageHistory);
    }

    private static void buildDocumentHistory(
        CaseData caseData,
        MessageSendDetails sendMessagesDetails,
        List<ListValue<Document>> documentHistory
    ) {
        if (caseData.getAttachDocumentList() != null
            && caseData.getAttachDocumentList().getValue() != null) {

            CaseEventCommonMethods.prepareDocumentList(caseData).stream()
                .filter(item -> item.getMessageId().equalsIgnoreCase(
                    caseData.getAttachDocumentList().getValue().getCode().toString()
                ))
                .findFirst()
                .ifPresent(selectedDocument -> {
                    Document document = selectedDocument.getDocumentLink();

                    sendMessagesDetails.setSelectedDocument(document);
                    sendMessagesDetails.setDocumentHistory(archiveListHelper(documentHistory, document));
                });
        }
    }

    private static <T> List<T> emptyIfNull(List<T> items) {
        return items == null ? List.of() : items;
    }
}
