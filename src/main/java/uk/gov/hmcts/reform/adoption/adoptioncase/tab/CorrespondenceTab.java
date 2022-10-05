package uk.gov.hmcts.reform.adoption.adoptioncase.tab;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.Tab;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.CASE_WORKER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.DISTRICT_JUDGE;

@Component
public class CorrespondenceTab implements CCDConfig<CaseData, State, UserRole> {
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        final Tab.TabBuilder<CaseData, UserRole> tabBuilder = configBuilder.tab("applicationCorrespondence", "Correspondence")
            .forRoles(CASE_WORKER, DISTRICT_JUDGE);
        addCorrespondence(tabBuilder);
    }


    private void addCorrespondence(final Tab.TabBuilder<CaseData, UserRole> tabBuilder) {
        tabBuilder
            .label("LabelNotes-Correspondence", null, "### Correspondence")
            .label("Upload correspondence",
                   null,
                   "[Upload correspondence](/cases/case-details/${[CASE_REFERENCE]}"
                       + "/trigger/caseworker-manage-document/caseworker-manage-documentuploadDocument)"
            )
            .field(CaseData::getCorrespondenceDocumentCategory);
    }
}

