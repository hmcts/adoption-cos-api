package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.reform.adoption.adoptioncase.common.CommonPageBuilder;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

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
            .mandatoryWithLabel(MessageSendDetails::getMessageReceiverRoles,"Who do you want to send a message to?")
            .mandatoryWithLabel(MessageSendDetails::getMessageReasonList,"Select a reason for this message")
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
        CommonPageBuilder.sendOrReplyCommonPage(pageBuilder, "");

    }

}
