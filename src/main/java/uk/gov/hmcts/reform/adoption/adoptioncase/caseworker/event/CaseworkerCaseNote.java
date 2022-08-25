package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

/**
 * Adds a Case Note Event in ExUI for all States.
 * This will enable users to add Note(s) for a Case.
 */
@Slf4j
@Component
public class CaseworkerCaseNote implements CCDConfig<CaseData, State, UserRole> {

    public static final String CASEWORKER_ADD_CASE_NOTE = "caseworker-add-casenote";

    public static final String ADD_CASE_NOTE = "Add a case note";

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        new PageBuilder(configBuilder
                            .event(CASEWORKER_ADD_CASE_NOTE)
                            .forAllStates()
                            .name(ADD_CASE_NOTE)
                            .description(ADD_CASE_NOTE)
                            .showSummary()
                            .grant(Permissions.CREATE_READ_UPDATE, UserRole.CASE_WORKER))
                            .page("addCaseNotes")
                            .pageLabel("Add case notes")
                            .mandatory(CaseData::getCaseNote);
    }
}
