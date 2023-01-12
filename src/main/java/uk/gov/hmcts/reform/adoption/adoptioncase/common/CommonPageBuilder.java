package uk.gov.hmcts.reform.adoption.adoptioncase.common;

import org.apache.commons.collections4.CollectionUtils;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MessageSendDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.SelectedMessage;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static uk.gov.hmcts.reform.adoption.adoptioncase.common.CaseEventCommonMethods.getMessageReasonLabel;

public final class CommonPageBuilder {

    private CommonPageBuilder() {
    }

    public static void sendOrReplyCommonPage(PageBuilder pageBuilder, String type) {
        if ("".equalsIgnoreCase(type)) {
            pageBuilder
                .page("pageSendOrReply1", CommonPageBuilder::sendMessageMidEvent)
                .showCondition(type)
                .mandatory(CaseData::getMessageAction)
                .readonly(CaseData::getLoggedInUserRole,"messageAction=\"judge\"")
                .mandatory(CaseData::getReplyMsgDynamicList, "messageAction=\"replyMessage\"");
            replyMessageBuilder(pageBuilder, "messageAction=\"replyMessage\"");
            messageBuilder(pageBuilder, "messageAction=\"sendMessage\" OR replyMessage=\"Yes\"");
        } else {
            pageBuilder.page("pageSendOrReply33")
                .showCondition(type)
                .label("sendMessageLab1", "## Send a message")
                .complex(CaseData::getMessageSendDetails)
                .mandatory(MessageSendDetails::getMessageReceiverRoles)
                .mandatory(MessageSendDetails::getMessageReasonList,"loggedInUserRole=\"default\"")
                .mandatory(MessageSendDetails::getMessageReasonJudge,"loggedInUserRole=\"judge\"")
                .mandatory(MessageSendDetails::getMessageUrgencyList)
                .done()
                .mandatory(CaseData::getSendMessageAttachDocument)
                .mandatory(CaseData::getAttachDocumentList, "sendMessageAttachDocument=\"Yes\"")
                .complex(CaseData::getMessageSendDetails)
                .mandatory(MessageSendDetails::getMessageText)
                .done();
        }
    }

    public static void messageBuilder(PageBuilder pageBuilder,String condition) {
        pageBuilder.page("pageSendOrReply3")
            .showCondition(condition)
            .label("sendMessageLab", "## Send a message","messageAction=\"sendMessage\"")
            .label("replyMessageLab", "## Reply to message","messageAction=\"replyMessage\"")
            .complex(CaseData::getMessageSendDetails)
            .mandatoryWithLabel(MessageSendDetails::getMessageReceiverRoles,"Who do you want to send a message to?")
            .mandatory(MessageSendDetails::getMessageReasonList,"loggedInUserRole=\"default\"")
            .mandatory(MessageSendDetails::getMessageReasonJudge,"loggedInUserRole=\"judge\"")
            .mandatory(MessageSendDetails::getMessageUrgencyList)
            .done()
            .mandatory(CaseData::getSendMessageAttachDocument)
            .mandatory(CaseData::getAttachDocumentList, "sendMessageAttachDocument=\"Yes\"")
            .complex(CaseData::getMessageSendDetails)
            .mandatory(MessageSendDetails::getMessageText)
            .done()
            .done();
    }

    public static void replyMessageBuilder(PageBuilder pageBuilder, String condition) {
        pageBuilder.page("pageSendOrReply2")
            .showCondition(condition)
            .label("labelReplyMes", "## Reply to message")
            .complex(CaseData::getSelectedMessage)
            .readonly(SelectedMessage::getReasonForMessage)
            .readonly(SelectedMessage::getUrgency)
            .readonly(SelectedMessage::getMessageContent)
            .readonly(SelectedMessage::getDocumentLink)
            .mandatory(SelectedMessage::getReplyMessage)
            .label("replyMessageNoConfirmation", "**No** <br> This message will now be marked as closed", "replyMessage=\"No\"")
            .done();
    }

    public static AboutToStartOrSubmitResponse<CaseData, State> sendMessageMidEvent(CaseDetails<CaseData, State> details,
                                                                   CaseDetails<CaseData, State> detailsBefore) {
        CaseData caseData = details.getData();
        List<DynamicListElement> listElements = new ArrayList<>();
        CaseEventCommonMethods.prepareDocumentList(caseData).forEach(item -> listElements.add(DynamicListElement.builder()
                                                            .label(item.getDocumentLink().getFilename())
                                                             .code(UUID.fromString(item.getMessageId())).build()));
        caseData.setAttachDocumentList(DynamicList.builder().listItems(listElements).value(DynamicListElement.EMPTY).build());

        if (CollectionUtils.isNotEmpty(caseData.getListOfOpenMessages()) && caseData.getReplyMsgDynamicList() != null) {
            setSelectedObject(caseData);
        }
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .build();
    }

    public static void setSelectedObject(CaseData caseData) {
        var messageDetails = new SelectedMessage();
        var selectedObject = caseData.getListOfOpenMessages().stream()
            .filter(item -> item.getValue().getMessageId().equalsIgnoreCase(caseData.getReplyMsgDynamicList()
                                                                                .getValueCode().toString())).findFirst();
        if (selectedObject.isPresent()) {
            messageDetails.setMessageId(selectedObject.get().getId());
            messageDetails.setUrgency(selectedObject.get().getValue().getMessageUrgencyList().getLabel());
            messageDetails.setMessageContent(selectedObject.get().getValue().getMessageText());
            messageDetails.setReasonForMessage(getMessageReasonLabel(selectedObject.get().getValue()));
            if (!Objects.isNull(selectedObject.get().getValue().getSelectedDocument())) {
                messageDetails.setDocumentLink(selectedObject.get().getValue().getSelectedDocument());
            }
            caseData.setSelectedMessage(messageDetails);
        }
    }
}
