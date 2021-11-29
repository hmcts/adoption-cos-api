package uk.gov.hmcts.reform.adoption.systemupdate.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.divorcecase.model.Application;
import uk.gov.hmcts.reform.adoption.divorcecase.model.CaseData;
import uk.gov.hmcts.reform.adoption.divorcecase.model.State;
import uk.gov.hmcts.reform.adoption.divorcecase.model.UserRole;

import static uk.gov.hmcts.reform.adoption.divorcecase.model.UserRole.SYSTEMUPDATE;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.access.Permissions.CREATE_READ_UPDATE;

@Component
@Slf4j
public class SystemUpdateIssueDate implements CCDConfig<CaseData, State, UserRole> {

    public static final String CASEWORKER_SYSTEM_USER_UPDATE_ISSUE_DATE = "system-update-issue-date";

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        new PageBuilder(configBuilder
            .event(CASEWORKER_SYSTEM_USER_UPDATE_ISSUE_DATE)
            .forAllStates()
            .name("Update issue date")
            .description("Update issue date")
            .explicitGrants()
            .grant(CREATE_READ_UPDATE, SYSTEMUPDATE))
            .page("updateIssueDate")
            .complex(CaseData::getApplication)
                .mandatory(Application::getIssueDate)
                .done();
    }
}
