package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.common.CaseEventCommonMethods;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MessageSendDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.SendOrReply;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.idam.client.models.User;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.COMMA;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.SEND_N_REPLY_DATE_FORMAT;

@Slf4j
@Component
public class CaseworkerSendOrReply implements CCDConfig<CaseData, State, UserRole> {

    public static final String CASEWORKER_SEND_OR_REPLY = "caseworker-send-or-reply";

    public static final String SEND_OR_REPLY_HEADING = "Send or reply to messages";

    private final CcdPageConfiguration sendOrReply = new SendOrReply();

    @Autowired
    private IdamService idamService;

    @Autowired
    private HttpServletRequest request;


    @Override
    public void configure(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        var pageBuilder = addConfig(configBuilder);
        sendOrReply.addTo(pageBuilder);
    }

    private PageBuilder addConfig(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.grant(State.Draft, Permissions.READ_UPDATE, UserRole.CASE_WORKER, UserRole.COURT_ADMIN,
                            UserRole.LEGAL_ADVISOR, UserRole.DISTRICT_JUDGE
        );
        return new PageBuilder(configBuilder
                                   .event(CASEWORKER_SEND_OR_REPLY)
                                   .forAllStates()
                                   .name(SEND_OR_REPLY_HEADING)
                                   .description(SEND_OR_REPLY_HEADING)
                                   .showSummary()
                                   .grant(Permissions.CREATE_READ_UPDATE, UserRole.CASE_WORKER)
                                   .grant(Permissions.CREATE_READ_UPDATE, UserRole.DISTRICT_JUDGE)
                                   .aboutToStartCallback(this::beforeStartEvent)
                                   .aboutToSubmitCallback(this::aboutToSubmit));
    }

    public AboutToStartOrSubmitResponse<CaseData, State> beforeStartEvent(CaseDetails<CaseData, State> details) {
        var caseData = details.getData();

        List<DynamicListElement> replyMessageList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(caseData.getListOfOpenMessages())) {
            caseData.getListOfOpenMessages().forEach(item -> {
                if (item.getValue().getMessageStatus().equals(MessageSendDetails.MessageStatus.OPEN)) {
                    DynamicListElement orderInfo = DynamicListElement.builder()
                        .label(item.getValue().getMessageSendDateNTime().format(
                                DateTimeFormatter.ofPattern(
                                    SEND_N_REPLY_DATE_FORMAT)).concat(COMMA)
                                   .concat(item.getValue().getMessageReasonList().getLabel())).code(
                            UUID.fromString(item.getValue().getMessageId())).build();
                    replyMessageList.add(orderInfo);
                }
            });

        }
        caseData.setReplyMsgDynamicList(DynamicList.builder().listItems(replyMessageList)
                                            .value(DynamicListElement.EMPTY).build());
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .build();
    }

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(CaseDetails<CaseData, State> details,
                                                                       CaseDetails<CaseData, State> detailsBefore) {

        final User caseworkerUser = idamService.retrieveUser(request.getHeader(AUTHORIZATION));
        var caseData = details.getData();
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
        caseData.setMessageAction(null);
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .build();
    }

    private String buildMessageHistory(User caseworkerUser, String messageText, String messageHistory) {
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

    private ListValue<MessageSendDetails> getSelectedMessage(CaseData caseData, String activeMessageID) {
        return caseData.getListOfOpenMessages().stream().filter(item -> item.getValue().getMessageId().equalsIgnoreCase(
            activeMessageID)).findFirst().get();
    }

    private void setMessageInformation(CaseData caseData, MessageSendDetails sendMessagesDetails, User caseworkerUser) {
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

    private void buildDocumentHistory(CaseData caseData, MessageSendDetails sendMessagesDetails,
                                      List<ListValue<Document>> documentHistory) {
        if (caseData.getAttachDocumentList() != null
            && caseData.getAttachDocumentList().getValue() != null) {
            var doc = CaseEventCommonMethods.prepareDocumentList(caseData).stream()
                .filter(item -> item.getMessageId().equalsIgnoreCase(caseData.getAttachDocumentList().getValue().getCode().toString()))
                .findFirst().get().getDocumentLink();
            sendMessagesDetails.setSelectedDocument(doc);
            sendMessagesDetails.setDocumentHistory(
                caseData.archiveManageOrdersHelper(documentHistory, doc));
        }
    }

}
