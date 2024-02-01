package uk.gov.hmcts.reform.adoption.adoptioncase.common;

import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.SelectedMessage;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public final class CommonPageBuilder {

    private static final String REPLY_MESSAGE = "messageAction=\"replyMessage\"";

    private CommonPageBuilder() {
    }

    public static void sendOrReplyCommonPage(PageBuilder pageBuilder, String type) {
        if ("".equalsIgnoreCase(type)) {
            pageBuilder
                .page("pageSendOrReply1", CommonPageBuilder::sendMessageMidEvent)
                .showCondition(type)
                //.mandatory(CaseData::getMessageAction)
                .readonly(CaseData::getLoggedInUserRole,"messageAction=\"judge\"");
            //.mandatory(CaseData::getReplyMsgDynamicList, REPLY_MESSAGE);
            replyMessageBuilder(pageBuilder, REPLY_MESSAGE);
            messageBuilder(pageBuilder, "messageAction=\"sendMessage\" OR replyMessage=\"Yes\"");
        } else {
            pageBuilder.page("pageSendOrReply33")
                .showCondition(type)
                .label("sendMessageLab1", "## Send a message")
                .done();
        }
    }

    public static void messageBuilder(PageBuilder pageBuilder,String condition) {
        pageBuilder.page("pageSendOrReply3")
            .showCondition(condition)
            .label("sendMessageLab", "## Send a message","messageAction=\"sendMessage\"")
            .label("replyMessageLab", "## Reply to message",REPLY_MESSAGE)
            .done();
    }

    public static void replyMessageBuilder(PageBuilder pageBuilder, String condition) {
        pageBuilder.page("pageSendOrReply2")
            .showCondition(condition)
            .label("labelReplyMes", "## Reply to message")

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



        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .build();
    }

    public static void setSelectedObject(CaseData caseData) {
        var messageDetails = new SelectedMessage();
    }

}
