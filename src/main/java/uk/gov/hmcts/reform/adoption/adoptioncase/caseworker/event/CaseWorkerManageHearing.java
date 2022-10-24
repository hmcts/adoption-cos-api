package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.ManageHearings;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageHearingOptions;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

@Component
@Slf4j
public class CaseWorkerManageHearing implements CCDConfig<CaseData, State, UserRole> {

    /**
     * The constant CASEWORKER_MANAGE_HEARINGS.
     */
    public static final String CASEWORKER_MANAGE_HEARING = "caseworker-manage-hearing";

    /**
     * The constant MANAGE_HEARINGS.
     */
    public static final String MANAGE_HEARINGS = "Manage hearings";

    private final CcdPageConfiguration manageHearings = new ManageHearings();


    @Override
    public void configure(ConfigBuilder<CaseData, State, UserRole> configBuilder) {

        log.info("Inside configure method for Event {}", CASEWORKER_MANAGE_HEARING);
        var pageBuilder = addEventConfig(configBuilder);
        manageHearings.addTo(pageBuilder);

    }

    /**
     * Helper method to make custom changes to the CCD Config in order to add the event to respective Page Configuration.
     *
     * @param configBuilder - Base CCD Config Builder updated to add Event for Page
     * @return - PageBuilder updated to use on overridden method.
     */
    private PageBuilder addEventConfig(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.grant(State.Draft, Permissions.READ_UPDATE, UserRole.CASE_WORKER, UserRole.COURT_ADMIN,
                            UserRole.LEGAL_ADVISOR, UserRole.DISTRICT_JUDGE
        );
        return new PageBuilder(configBuilder
                                   .event(CASEWORKER_MANAGE_HEARING)
                                   .forAllStates()
                                   .name(MANAGE_HEARINGS)
                                   .description(MANAGE_HEARINGS)
                                   .showSummary()
                                   .aboutToSubmitCallback(this::aboutToSubmit)
                                   .grant(Permissions.CREATE_READ_UPDATE, UserRole.CASE_WORKER)
                                   .grant(Permissions.CREATE_READ_UPDATE, UserRole.DISTRICT_JUDGE));
    }

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(
        CaseDetails<CaseData, State> details,
        CaseDetails<CaseData, State> beforeDetails) {

        log.info("Callback invoked for {}", CASEWORKER_MANAGE_HEARING);
        var caseData = details.getData();
        if (ManageHearingOptions.ADD_NEW_HEARING.equals(caseData.getManageHearingOptions())) {
            caseData.archiveHearingInformation();
        } else if (ManageHearingOptions.VACATE_HEARING.equals(caseData.getManageHearingOptions())) {
            caseData.updateVacatedHearings();
        }
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .build();
    }


}
