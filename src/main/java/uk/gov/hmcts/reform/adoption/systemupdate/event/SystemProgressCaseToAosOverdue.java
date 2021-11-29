package uk.gov.hmcts.reform.adoption.systemupdate.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.reform.adoption.citizen.notification.ApplicationIssuedNotification;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.divorcecase.model.CaseData;
import uk.gov.hmcts.reform.adoption.divorcecase.model.State;
import uk.gov.hmcts.reform.adoption.divorcecase.model.UserRole;

import java.util.Objects;

import static java.util.EnumSet.of;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.State.AosDrafted;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.State.AosOverdue;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.State.AwaitingAos;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.UserRole.CASE_WORKER;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.UserRole.LEGAL_ADVISOR;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.UserRole.SOLICITOR;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.UserRole.SUPER_USER;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.UserRole.SYSTEMUPDATE;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.access.Permissions.CREATE_READ_UPDATE;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.access.Permissions.READ;

@Component
public class SystemProgressCaseToAosOverdue implements CCDConfig<CaseData, State, UserRole> {

    public static final String SYSTEM_PROGRESS_TO_AOS_OVERDUE = "system-progress-to-aos-overdue";

    @Autowired
    private ApplicationIssuedNotification applicationIssuedNotification;

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        new PageBuilder(configBuilder
            .event(SYSTEM_PROGRESS_TO_AOS_OVERDUE)
            .forStateTransition(of(AwaitingAos, AosDrafted), AosOverdue)
            .name("AoS not received within SLA")
            .description("AoS not received within SLA")
            .explicitGrants()
            .grant(CREATE_READ_UPDATE, SYSTEMUPDATE)
            .grant(READ, SOLICITOR, CASE_WORKER, SUPER_USER, LEGAL_ADVISOR)
            .aboutToSubmitCallback(this::aboutToSubmit));
    }

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(CaseDetails<CaseData, State> details,
                                                                       CaseDetails<CaseData, State> beforeDetails) {

        CaseData data = details.getData();
        if (!Objects.isNull(data.getApplicant2EmailAddress())
            && !Objects.isNull(data.getCaseInvite().getAccessCode())) {
            applicationIssuedNotification.sendReminderToSoleRespondent(data, details.getId());
        }

        if (!data.getApplication().isSolicitorApplication()) {
            applicationIssuedNotification.sendPartnerNotRespondedToSoleApplicant(data, details.getId());
        }

        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(data)
            .build();
    }
}
