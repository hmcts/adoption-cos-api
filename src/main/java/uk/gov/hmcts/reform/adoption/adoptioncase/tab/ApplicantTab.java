package uk.gov.hmcts.reform.adoption.adoptioncase.tab;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.Tab;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

@Component
public class ApplicantTab implements CCDConfig<CaseData, State, UserRole> {


    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        final Tab.TabBuilder<CaseData, UserRole> tabBuilder = configBuilder.tab("applicantDetails", "Applicants");

        addHeaderFields(tabBuilder);
    }

    public void addHeaderFields(final Tab.TabBuilder<CaseData, UserRole> tabBuilder) {
        tabBuilder
            .label("headingApplicantDetails", null, "### Applicants Details")
            .field("status")
            .field("message");
    }

}
