package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.AddJudge;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;


@Slf4j
@Component
public class CaseWorkerAllocateJudge implements CCDConfig<CaseData, State, UserRole> {

    public static final String CASEWORKER_ALLOCATE_JUDGE = "caseworker-allocate-judge";

    public static final String ALLOCATE_JUDGE = "Allocate judge";

    private final CcdPageConfiguration allocateJudge = new AddJudge();


    @Override
    public void configure(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        log.info("Inside configure method for Event {}", CASEWORKER_ALLOCATE_JUDGE);
        allocateJudge.addTo(addConfig(configBuilder));
    }


    /***
     *
     * @param configBuilder
     */
    private PageBuilder addConfig(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.grant(State.Draft, Permissions.READ_UPDATE, UserRole.CASE_WORKER, UserRole.COURT_ADMIN,
                            UserRole.LEGAL_ADVISOR, UserRole.DISTRICT_JUDGE
        );
        return new PageBuilder(configBuilder
                                   .event(CASEWORKER_ALLOCATE_JUDGE)
                                   .forAllStates()
                                   .name(ALLOCATE_JUDGE)
                                   .description(ALLOCATE_JUDGE)
                                   .showSummary()
                                   .grant(Permissions.CREATE_READ_UPDATE, UserRole.CASE_WORKER)
                                   .aboutToSubmitCallback(this::aboutToSubmit));
    }

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(CaseDetails<CaseData, State>
        details, CaseDetails<CaseData, State>
        beforeDetails) {
        var caseData = details.getData();
        caseData.setAllocatedJudge(caseData.getAllocatedJudge());
    return AboutToStartOrSubmitResponse.<CaseData, State>builder()
        .data(details.getData())
        .build();
    }
}
