package uk.gov.hmcts.reform.adoption.adoptioncase.tab;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.Tab;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

@Component
public class SummaryTab implements CCDConfig<CaseData, State, UserRole> {


    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        final Tab.TabBuilder<CaseData, UserRole> tabBuilder = configBuilder.tab("applicationSummary", "Summary");

        addHeaderFields(tabBuilder);
        addApplicant(tabBuilder);
    }

    public void addHeaderFields(final Tab.TabBuilder<CaseData, UserRole> tabBuilder) {
        tabBuilder
            .label("headingCaseStatus", null, "### Case status")
            .field("status")
            .field("message");
    }

    private void addApplicant(final Tab.TabBuilder<CaseData, UserRole> tabBuilder) {
        tabBuilder
            .label("caseDetails"  , null, "### Case details")
            .field("typeOfAdoption")
            .field("dateSubmitted")
            .field("dateChildMovedIn")
            .field("applicationPayments")
            .field("placementOrders")
            .field("socialWorkerDetails")
            .field("applicationPayments");
    }

}
