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
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.SendAndReply;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MessagesAction;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class CaseworkerSendAndReply implements CCDConfig<CaseData, State, UserRole> {

    public static final String CASEWORKER_SEND_AND_REPLY = "caseworker-send-and-reply";

    public static final String SEND_AND_REPLY_HEADING = "Send And Reply to messages";

    private final CcdPageConfiguration sendAndReply = new SendAndReply();


    @Override
    public void configure(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        log.info("Inside configure method for Event {}", SEND_AND_REPLY_HEADING);
        var pageBuilder = addConfig(configBuilder);
        sendAndReply.addTo(pageBuilder);
    }

    private PageBuilder addConfig(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.grant(State.Draft, Permissions.READ_UPDATE, UserRole.CASE_WORKER, UserRole.COURT_ADMIN,
                            UserRole.LEGAL_ADVISOR, UserRole.DISTRICT_JUDGE
        );
        return new PageBuilder(configBuilder
                                   .event(CASEWORKER_SEND_AND_REPLY)
                                   .forAllStates()
                                   .name(SEND_AND_REPLY_HEADING)
                                   .description(SEND_AND_REPLY_HEADING)
                                   .showSummary()
                                   .grant(Permissions.CREATE_READ_UPDATE, UserRole.CASE_WORKER)
                                   .grant(Permissions.CREATE_READ_UPDATE, UserRole.DISTRICT_JUDGE)
                                   .aboutToStartCallback(this::beforeMethod)
                                   .aboutToSubmitCallback(this::aboutToSubmit));
    }

    private AboutToStartOrSubmitResponse<CaseData, State> beforeMethod(CaseDetails<CaseData, State> details) {
        var caseData = details.getData();
        List<DynamicListElement> listElements = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(caseData.getListOfSendMessages())) {
            caseData.getListOfSendMessages().forEach(item -> {
                DynamicListElement listElement = DynamicListElement.builder()
                    .label(item.getValue().getMessageId() != null ? item.getValue().getMessageId() : "No MESSAGE ID")
                    .code(item.getValue().getMessageId() != null
                    ? UUID.fromString(item.getValue().getMessageId()) : UUID.randomUUID()).build();
                listElements.add(listElement);
            });

        }
        caseData.setMessagesList(DynamicList.builder().listItems(listElements).value(DynamicListElement.EMPTY).build());
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .build();
    }

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(CaseDetails<CaseData, State> caseDataStateCaseDetails,
                                                                       CaseDetails<CaseData, State> caseDataStateCaseDetails1) {
        var caseData = caseDataStateCaseDetails.getData();

        if (caseData.getMessageAction().equals(MessagesAction.SEND_A_MESSAGE)) {
            caseData.storeSendMessages();
        }
        caseData.setMessageAction(null);
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseDataStateCaseDetails.getData())
            .build();
    }
}
