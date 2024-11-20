package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.AmendCaseDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

/**
 * Contains method to define Event Configuration for ExUI.
 * Enable Amend Case Details functionality for Adoption Cases.
 */
@Slf4j
@Component
public class CaseworkerAmendCase implements CCDConfig<CaseData, State, UserRole> {
    public static final String AMEND_CASE_DETAILS = "Amend case details";
    public static final String CASEWORKER_AMEND_CASE = "caseworker-amend-case";

    private final AmendCaseDetails amendCaseDetails = new AmendCaseDetails();

    @Override
    public void configure(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        log.info("Inside configure method for Event {}", CASEWORKER_AMEND_CASE);
        var pageBuilder = addEventConfig(configBuilder);
        amendCaseDetails.addTo(pageBuilder);
    }

    /**
     * Helper method to make custom changes to the CCD Config in order to add the event to respective Page Configuration.
     *
     * @param configBuilder - Base CCD Config Builder updated to add Event for Page
     * @return - PageBuilder updated to use on overridden method.
     */
    private PageBuilder addEventConfig(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        return new PageBuilder(configBuilder
                                   .event(CASEWORKER_AMEND_CASE)
                                   .forAllStates()
                                   .name(AMEND_CASE_DETAILS)
                                   .description(AMEND_CASE_DETAILS)
                                   .showSummary()
        );
    }
}
