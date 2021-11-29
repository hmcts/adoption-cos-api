package uk.gov.hmcts.reform.adoption.systemupdate.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.divorcecase.model.CaseData;
import uk.gov.hmcts.reform.adoption.divorcecase.model.State;
import uk.gov.hmcts.reform.adoption.divorcecase.model.UserRole;

import static uk.gov.hmcts.reform.adoption.divorcecase.model.State.AosOverdue;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.State.AwaitingAos;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.State.Holding;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.UserRole.CASE_WORKER;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.UserRole.LEGAL_ADVISOR;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.UserRole.SOLICITOR;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.UserRole.SUPER_USER;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.UserRole.SYSTEMUPDATE;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.access.Permissions.CREATE_READ_UPDATE;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.access.Permissions.READ;

@Component
@Slf4j
public class SystemIssueSolicitorAosDisputed implements CCDConfig<CaseData, State, UserRole> {

    public static final String SYSTEM_ISSUE_SOLICITOR_AOS_DISPUTED = "system-issue-solicitor-aos-disputed";

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {

        new PageBuilder(configBuilder
            .event(SYSTEM_ISSUE_SOLICITOR_AOS_DISPUTED)
            .forStates(AwaitingAos, Holding, AosOverdue)
            .name("AoS disputed")
            .description("AoS disputed")
            .explicitGrants()
            .grant(CREATE_READ_UPDATE, SYSTEMUPDATE)
            .grant(READ, SOLICITOR, CASE_WORKER, SUPER_USER, LEGAL_ADVISOR)
            .retries(120, 120));
    }
}
