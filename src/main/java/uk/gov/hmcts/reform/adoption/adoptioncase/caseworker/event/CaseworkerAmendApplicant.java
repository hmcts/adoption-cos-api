package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.AmendApplicantDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

/**
 * Contains method to define Event Configuration for ExUI.
 * Enable Amend Applicant Details functionality for Adoption Cases.
 */
@Slf4j
@Component
public class CaseworkerAmendApplicant implements CCDConfig<CaseData, State, UserRole> {
    public static final String AMEND_APPLICANT_DETAILS = "Amend applicant details";
    public static final String CASEWORKER_AMEND_APPLICANT = "caseworker-amend-applicant";

    private final AmendApplicantDetails amendApplicantDetails = new AmendApplicantDetails();

    @Override
    public void configure(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        var pageBuilder = addEventConfig(configBuilder);
        amendApplicantDetails.addTo(pageBuilder);
    }

    /**
     * Helper method to make custom changes to the CCD Config in order to add the event to respective Page Configuration.
     *
     * @param configBuilder - Base CCD Config Builder updated to add Event for Page
     * @return - PageBuilder updated to use on overridden method.
     */
    private PageBuilder addEventConfig(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        return new PageBuilder(configBuilder
                                   .event(CASEWORKER_AMEND_APPLICANT)
                                   .forAllStates()
                                   .name(AMEND_APPLICANT_DETAILS)
                                   .description(AMEND_APPLICANT_DETAILS)
                                   .showSummary()
                                   //.grant(Permissions.CREATE_READ_UPDATE, UserRole.CASE_WORKER)
                                   //.grant(Permissions.CREATE_READ_UPDATE, UserRole.DISTRICT_JUDGE)
        );
    }
}
