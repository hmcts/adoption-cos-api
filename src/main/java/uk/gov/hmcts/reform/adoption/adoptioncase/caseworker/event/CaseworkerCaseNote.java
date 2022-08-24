package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.AddCaseNote;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

import java.util.List;

@Slf4j
@Component
public class CaseworkerCaseNote implements CCDConfig<CaseData, State, UserRole> {

    public static final String CASEWORKER_ADD_CASE_NOTE = "caseworker-add-casenote";

    public static final String ADD_CASE_NOTE = "Add a case note";

    private final CcdPageConfiguration caseNote = new AddCaseNote();

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        log.info("Inside configure method for Event {}", CASEWORKER_ADD_CASE_NOTE);
        var pageBuilder = addEventConfig(configBuilder);
        caseNote.addTo(pageBuilder);
    }

    private PageBuilder addEventConfig(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.grant(State.Draft, Permissions.READ_UPDATE, UserRole.CASE_WORKER, UserRole.COURT_ADMIN,
                            UserRole.LEGAL_ADVISOR, UserRole.DISTRICT_JUDGE
        );
        return new PageBuilder(configBuilder
                                   .event(CASEWORKER_ADD_CASE_NOTE)
                                   .forStates(State.Draft)
                                   .name(ADD_CASE_NOTE)
                                   .description(ADD_CASE_NOTE)
                                   .showSummary()
                                   .grant(Permissions.CREATE_READ_UPDATE, UserRole.CASE_WORKER)
                                   .aboutToSubmitCallback(this::aboutToSubmit));
    }

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(CaseDetails<CaseData, State> details,
                                                                       CaseDetails<CaseData, State> beforeDetails) {

        log.info("{} about to submit callback invoked for Case Id: {}", CASEWORKER_ADD_CASE_NOTE, details.getId());
        CaseData caseData = details.getData();

        if (caseData.getCaseNote().getNote() != null
            || !caseData.getCaseNote().getNote().isEmpty()
            || !caseData.getCaseNote().getNote().isBlank()) {
            return AboutToStartOrSubmitResponse.<CaseData, State>builder()
                .data(caseData)
                .errors(List.of("Enter details for the case note"))
                .build();
        }

        if (caseData.getCaseNote().getSubject() != null
            || !caseData.getCaseNote().getSubject().isEmpty()
            || !caseData.getCaseNote().getSubject().isBlank()) {
            return AboutToStartOrSubmitResponse.<CaseData, State>builder()
                .data(caseData)
                .errors(List.of("Enter a subject for the case note"))
                .build();
        }



        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .build();

    }

}
