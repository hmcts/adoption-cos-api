package uk.gov.hmcts.reform.adoption.adoptioncase.tab;

import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.Tab;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

public class CorrespondenceTab implements CCDConfig<CaseData, State, UserRole> {
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        final Tab.TabBuilder<CaseData, UserRole> tabBuilder = configBuilder.tab("applicationCorrespondence", "Correspondence");
        addCorrespondence(tabBuilder);
    }

    private void addCorrespondence(final Tab.TabBuilder<CaseData, UserRole> tabBuilder) {
        tabBuilder
            .label("LabelNotes-Correspondence", null, "### Correspondence documents")
            .field(CaseData::getCaseNote);
    }
}
