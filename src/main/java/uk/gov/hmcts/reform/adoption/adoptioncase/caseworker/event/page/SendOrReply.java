package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.SelectedMessage;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MessageDocumentList;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MessageSendDetails;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SendOrReply implements CcdPageConfiguration {
    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder
            .page("pageSendOrReply1", this::midEvent)
            .mandatory(CaseData::getMessageAction)
            .mandatory(CaseData::getReplyMsgDynamicList, "messageAction=\"replyMessage\"");
        sendMessageBuilder(pageBuilder, "messageAction=\"sendMessage\"");
        replyMessageBuilder(pageBuilder, "messageAction=\"replyMessage\"");

    }

    public void sendMessageBuilder(PageBuilder pageBuilder, String condition) {
        pageBuilder.page("pageSendOrReply2")
            .showCondition(condition)
            .label("sendMessage", "messageAction=\"sendMessage\"")
            .complex(CaseData::getMessageSendDetails)
            .mandatory(MessageSendDetails::getMessageReceiverRoles)
            .mandatory(MessageSendDetails::getMessageReasonList)
            .mandatory(MessageSendDetails::getMessageUrgencyList)
            .mandatory(MessageSendDetails::getMessage)
            .done()
            .mandatory(CaseData::getSendMessageAttachDocument)
            .mandatory(CaseData::getAttachDocumentList, "sendMessageAttachDocument=\"Yes\"")
            .done();
    }

    public void replyMessageBuilder(PageBuilder pageBuilder, String condition) {
        pageBuilder.page("pageSendOrReply3")
            .showCondition(condition)
            .label("labelReplyMes", "## Reply a message")
            .complex(CaseData::getSelectedMessage)
            .readonly(SelectedMessage::getReasonForMessage)
            .readonly(SelectedMessage::getUrgency)
            .readonly(SelectedMessage::getMessage)
            .readonly(SelectedMessage::getDocumentLink)
            .mandatory(SelectedMessage::getReplyMessage)
            .done();
        pageBuilder.page("pageSendOrReply4")
            .showCondition("replyMessage=\"Yes\"")
            .label("sendMessage1", "## Reply to a message")
            .complex(CaseData::getMessageSendDetails)
            .mandatory(MessageSendDetails::getMessageReceiverRoles)
            .mandatory(MessageSendDetails::getMessageReasonList)
            .mandatory(MessageSendDetails::getMessageUrgencyList)
            .mandatory(MessageSendDetails::getMessage)
            .done()
            .mandatory(CaseData::getSendMessageAttachDocument)
            .mandatory(CaseData::getAttachDocumentList, "sendMessageAttachDocument=\"Yes\"")
            .done();


    }

    private AboutToStartOrSubmitResponse<CaseData, State> midEvent(CaseDetails<CaseData, State>
        data, CaseDetails<CaseData, State> caseDataStateCaseDetails1) {
        CaseData caseData = data.getData();
        List<DynamicListElement> listElements = new ArrayList<>();
        List<MessageDocumentList> messageDocumentLists = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(caseData.getAdditionalDocumentsCategory())) {
            caseData.getAdditionalDocumentsCategory().forEach(item -> {
                UUID result = UUID.nameUUIDFromBytes(item.getValue().getName().getBytes());
                listElements.add(DynamicListElement.builder()
                                     .label(item.getValue().getDocumentLink().getFilename()).code(result).build());
                messageDocumentLists.add(new MessageDocumentList(result.toString(), item.getValue().getDocumentLink()));
            });
        }

        if (CollectionUtils.isNotEmpty(caseData.getCorrespondenceDocumentCategory())) {
            caseData.getCorrespondenceDocumentCategory().forEach(item -> {
                if (item.getValue().getName() != null) {
                    UUID result = UUID.nameUUIDFromBytes(item.getValue().getName().getBytes());
                    listElements.add(DynamicListElement.builder()
                                         .label(item.getValue().getDocumentLink().getFilename()).code(result).build());
                    messageDocumentLists.add(new MessageDocumentList(result.toString(), item.getValue().getDocumentLink()));
                }
            });
        }

        if (CollectionUtils.isNotEmpty(caseData.getReportsDocumentCategory())) {
            caseData.getReportsDocumentCategory().forEach(item -> {
                UUID result = UUID.nameUUIDFromBytes(item.getValue().getName().getBytes());
                listElements.add(DynamicListElement.builder()
                                     .label(item.getValue().getDocumentLink().getFilename()).code(result).build());
                messageDocumentLists.add(new MessageDocumentList(result.toString(), item.getValue().getDocumentLink()));
            });
        }

        if (CollectionUtils.isNotEmpty(caseData.getStatementsDocumentCategory())) {
            caseData.getStatementsDocumentCategory().forEach(item -> {
                UUID result = UUID.nameUUIDFromBytes(item.getValue().getName().getBytes());
                listElements.add(DynamicListElement.builder()
                                     .label(item.getValue().getDocumentLink().getFilename()).code(result).build());
                messageDocumentLists.add(new MessageDocumentList(result.toString(), item.getValue().getDocumentLink()));
            });
        }

        if (CollectionUtils.isNotEmpty(caseData.getCourtOrdersDocumentCategory())) {
            caseData.getCourtOrdersDocumentCategory().forEach(item -> {
                UUID result = UUID.nameUUIDFromBytes(item.getValue().getName().getBytes());
                listElements.add(DynamicListElement.builder()
                                     .label(item.getValue().getDocumentLink().getFilename()).code(result).build());
            });
        }

        if (CollectionUtils.isNotEmpty(caseData.getApplicationDocumentsCategory())) {
            caseData.getApplicationDocumentsCategory().forEach(item -> {
                UUID result = UUID.nameUUIDFromBytes(item.getValue().getName().getBytes());
                listElements.add(DynamicListElement.builder()
                                     .label(item.getValue().getDocumentLink().getFilename()).code(result).build());
                messageDocumentLists.add(new MessageDocumentList(result.toString(), item.getValue().getDocumentLink()));
            });
        }
        caseData.setAttachDocumentList(DynamicList.builder().listItems(listElements).value(DynamicListElement.EMPTY).build());

        if (CollectionUtils.isNotEmpty(caseData.getListOfOpenMessages()) && caseData.getReplyMsgDynamicList() != null) {
            var messageDetails = new SelectedMessage();
            var selectedObject = caseData.getListOfOpenMessages().stream()
                .filter(item -> item.getValue().getMessageId().equalsIgnoreCase(caseData.getReplyMsgDynamicList()
                                                                               .getValueCode().toString())).findFirst();
            if (selectedObject.isPresent()) {
                messageDetails.setMessageId(selectedObject.get().getValue().getMessageId());
                messageDetails.setUrgency(selectedObject.get().getValue().getMessageUrgencyList().getLabel());
                messageDetails.setMessage(selectedObject.get().getValue().getMessage());
                messageDetails.setReasonForMessage(selectedObject.get().getValue().getMessageReasonList().getLabel());
                messageDetails.setDocumentLink(StringUtils.isNotEmpty(selectedObject.get().getValue().getSelectedDocumentId())
                                                   ? messageDocumentLists.stream().filter(item ->
                               item.getMessageId().equalsIgnoreCase(selectedObject.get().getValue().getSelectedDocumentId()))
                              .findFirst().get().getDocumentLink() : null);
                caseData.setSelectedMessage(messageDetails);
            }


        }
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .build();
    }

}
