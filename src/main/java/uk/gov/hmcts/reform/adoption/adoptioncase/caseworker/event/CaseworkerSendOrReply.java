package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.SendOrReply;
import uk.gov.hmcts.reform.adoption.adoptioncase.common.CaseEventCommonMethods;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MessageSendDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions;
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
import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.SEND_N_REPLY_USER_JUDGE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.SEND_N_REPLY_DATE_FORMAT;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.COMMA;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.SEND_N_REPLY_USER_CASEWORKER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

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
        final User caseworkerUser = idamService.retrieveUser(request.getHeader(AUTHORIZATION));
        CaseEventCommonMethods.prepareReplyMessageDynamicList(caseData);
        caseData.setLoggedInUserRole(caseworkerUser.getUserDetails().getRoles()
                                         .contains(UserRole.DISTRICT_JUDGE.getRole())
                                         ? SEND_N_REPLY_USER_JUDGE : SEND_N_REPLY_USER_CASEWORKER);
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .build();
    }

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(CaseDetails<CaseData, State> details,
                                                                       CaseDetails<CaseData, State> detailsBefore) {

        final User caseworkerUser = idamService.retrieveUser(request.getHeader(AUTHORIZATION));
        var caseData = details.getData();
        CaseEventCommonMethods.updateMessageList(caseData,caseworkerUser);
        caseData.setMessageAction(null);
        caseData.setLoggedInUserRole(null);
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .build();
    }

    private String getMessageReasonLabel(MessageSendDetails item) {
        if (item.getMessageReasonList() != null && item.getMessageReasonList().getLabel() != null) {
            return item.getMessageReasonList().getLabel();
        } else if (item.getMessageReasonJudge() != null && item.getMessageReasonJudge().getLabel() != null) {
            return item.getMessageReasonJudge().getLabel();
        }
        return null;
    }
}
