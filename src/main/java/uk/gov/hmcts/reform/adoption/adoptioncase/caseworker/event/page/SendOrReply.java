package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import org.apache.commons.collections4.CollectionUtils;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.reform.adoption.adoptioncase.common.CaseEventCommonMethods;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.SelectedMessage;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MessageSendDetails;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class SendOrReply implements CcdPageConfiguration {
    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder
            .page("pageSendOrReply1", this::midEvent)
            .mandatory(CaseData::getMessageAction)
            .mandatory(CaseData::getReplyMsgDynamicList, "messageAction=\"replyMessage\"");
        replyMessageBuilder(pageBuilder, "messageAction=\"replyMessage\"");
        messageBuilder(pageBuilder, "messageAction=\"sendMessage\" OR replyMessage=\"Yes\"");

    }

    public void messageBuilder(PageBuilder pageBuilder,String condition) {
        pageBuilder.page("pageSendOrReply3")
            .showCondition(condition)
            .label("sendMessageLab", "## Send a message","messageAction=\"sendMessage\"")
            .label("replyMessageLab", "## Reply to message","messageAction=\"replyMessage\"")
            .complex(CaseData::getMessageSendDetails)
            .mandatory(MessageSendDetails::getMessageReceiverRoles)
            .mandatory(MessageSendDetails::getMessageReasonList)
            .mandatory(MessageSendDetails::getMessageUrgencyList)
            .done()
            .mandatory(CaseData::getSendMessageAttachDocument)
            .mandatory(CaseData::getAttachDocumentList, "sendMessageAttachDocument=\"Yes\"")
            .complex(CaseData::getMessageSendDetails)
            .mandatory(MessageSendDetails::getMessageText)
            .done()
            .done();
    }

    public void replyMessageBuilder(PageBuilder pageBuilder, String condition) {
        pageBuilder.page("pageSendOrReply2")
            .showCondition(condition)
            .label("labelReplyMes", "## Reply to message")
            .complex(CaseData::getSelectedMessage)
            .readonly(SelectedMessage::getReasonForMessage)
            .readonly(SelectedMessage::getUrgency)
            .readonly(SelectedMessage::getMessageContent)
            .readonly(SelectedMessage::getDocumentLink)
            .mandatory(SelectedMessage::getReplyMessage)
            .done();

    }

    private AboutToStartOrSubmitResponse<CaseData, State> midEvent(CaseDetails<CaseData, State> details,
                                                                   CaseDetails<CaseData, State> detailsBefore) {
        CaseData caseData = details.getData();
        List<DynamicListElement> listElements = new ArrayList<>();
        CaseEventCommonMethods.prepareDocumentList(caseData).forEach(item -> listElements.add(DynamicListElement.builder()
                             .label(item.getDocumentLink().getFilename()).code(UUID.fromString(item.getMessageId())).build()));
        caseData.setAttachDocumentList(DynamicList.builder().listItems(listElements).value(DynamicListElement.EMPTY).build());

        if (CollectionUtils.isNotEmpty(caseData.getListOfOpenMessages()) && caseData.getReplyMsgDynamicList() != null) {
            var messageDetails = new SelectedMessage();
            var selectedObject = caseData.getListOfOpenMessages().stream()
                .filter(item -> item.getValue().getMessageId().equalsIgnoreCase(caseData.getReplyMsgDynamicList()
                                                                               .getValueCode().toString())).findFirst();
            messageDetails.setMessageId(selectedObject.get().getId().toString());
            messageDetails.setUrgency(selectedObject.get().getValue().getMessageUrgencyList().getLabel());
            messageDetails.setMessageContent(selectedObject.get().getValue().getMessageText());
            messageDetails.setReasonForMessage(selectedObject.get().getValue().getMessageReasonList().getLabel());
            if (!Objects.isNull(selectedObject.get().getValue().getSelectedDocument())) {
                messageDetails.setDocumentLink(selectedObject.get().getValue().getSelectedDocument());
            }
            caseData.setSelectedMessage(messageDetails);
        }
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .build();
    }



}
