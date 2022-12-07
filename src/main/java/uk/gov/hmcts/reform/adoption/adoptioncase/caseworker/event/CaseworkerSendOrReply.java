package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.SendOrReply;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MessageSendDetails;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.COMMA;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.SEND_N_REPLY_DATE_FORMAT;

@Slf4j
@Component
public class CaseworkerSendOrReply implements CCDConfig<CaseData, State, UserRole> {

    public static final String CASEWORKER_SEND_OR_REPLY = "caseworker-send-or-reply";

    public static final String SEND_OR_REPLY_HEADING = "Send or reply to messages";

    private final CcdPageConfiguration sendOrReply = new SendOrReply();


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

    private AboutToStartOrSubmitResponse<CaseData, State> beforeStartEvent(CaseDetails<CaseData, State> details) {
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

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(CaseDetails<CaseData, State> caseDataStateCaseDetails,
                                                                       CaseDetails<CaseData, State> caseDataStateCaseDetails1) {
        var caseData = caseDataStateCaseDetails.getData();
        if (caseData.getMessageAction().equals(MessageSendDetails.MessagesAction.SEND_A_MESSAGE)) {
            MessageSendDetails sendMessagesDetails = caseData.getMessageSendDetails();
            sendMessagesDetails.setMessageId(UUID.randomUUID().toString());
            if (caseData.getAttachDocumentList() != null
                && caseData.getAttachDocumentList().getValue() != null) {
                sendMessagesDetails.setSelectedDocumentId(caseData.getAttachDocumentList().getValue().getCode().toString());
            }
            sendMessagesDetails.setMessageStatus(MessageSendDetails.MessageStatus.OPEN);
            sendMessagesDetails.setMessageSendDateNTime(
                LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
            caseData.setListOfOpenMessages(caseData.archiveManageOrdersHelper(
                caseData.getListOfOpenMessages(), sendMessagesDetails));
            caseData.setMessageSendDetails(null);
            caseData.setSelectedMessage(null);
        }
        caseData.setMessageAction(null);
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseDataStateCaseDetails.getData())
            .build();
    }
}
