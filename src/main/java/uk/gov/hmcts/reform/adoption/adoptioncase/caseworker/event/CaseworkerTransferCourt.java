package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.TransferCourt;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

@Slf4j
@Component
public class CaseworkerTransferCourt implements CCDConfig<CaseData, State, UserRole>  {

    public static final String CASEWORKER_TRANSFER_COURT = "caseworker-tranfer-court";

    public static final String TRANSFER_COURT_HEADING = "Transfer Court";

    private final CcdPageConfiguration transferCourt = new TransferCourt();

    @Override
    public void configure(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        log.info("Inside configure method for Event {}", CASEWORKER_TRANSFER_COURT);
        var pageBuilder = addEventConfig(configBuilder);
        transferCourt.addTo(pageBuilder);
    }

    private PageBuilder addEventConfig(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        return new PageBuilder(configBuilder
                                   .event(CASEWORKER_TRANSFER_COURT)
                                   .forStates(State.LaSubmitted)
                                   .showCondition("applicant1Email=\"DO_NOT_SHOW\"")
                                   .name(TRANSFER_COURT_HEADING)
                                   .description(TRANSFER_COURT_HEADING)
                                   .showSummary()
                                   .grant(Permissions.CREATE_READ_UPDATE, UserRole.CASE_WORKER)
                                   .grant(Permissions.CREATE_READ_UPDATE, UserRole.DISTRICT_JUDGE)
                                   .aboutToSubmitCallback(this::aboutToSubmit));
    }

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(CaseDetails<CaseData, State> caseDataStateCaseDetails,
                                                                        CaseDetails<CaseData, State> caseDataStateCaseDetails1) {
        var caseData = caseDataStateCaseDetails.getData();
        caseData.setFamilyCourtName(caseData.getTransferCourt());
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseDataStateCaseDetails.getData())
            .build();
    }
}
