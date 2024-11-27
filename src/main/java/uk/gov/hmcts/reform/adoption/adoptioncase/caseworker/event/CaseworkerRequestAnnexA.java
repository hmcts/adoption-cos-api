package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;


import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.RequestAnnexA;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

@Component
public class CaseworkerRequestAnnexA implements CCDConfig<CaseData, State, UserRole> {
    public static final String CASEWORKER_AMEND_CASE = "caseworker-request-annex-a";
    private final CcdPageConfiguration requestAnnexA = new RequestAnnexA();


    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        var pageBuilder = addEventConfig(configBuilder);
        requestAnnexA.addTo(pageBuilder);
    }

    private PageBuilder addEventConfig(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        return new PageBuilder(configBuilder
                                   .event(CASEWORKER_AMEND_CASE)
                                   .forStates(State.LaSubmitted)
                                   .name("Request Annex-A")
                                   .description("Request Annex-A")
                                   .showSummary()
                                   .showEventNotes()
                                   .grant(Permissions.CREATE_READ_UPDATE, UserRole.CASE_WORKER)
                                   .grant(Permissions.CREATE_READ_UPDATE, UserRole.DISTRICT_JUDGE));
    }
}
