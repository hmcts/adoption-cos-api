package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.AddCaseNote;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseNote;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.idam.client.models.User;
import jakarta.servlet.http.HttpServletRequest;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Adds a Case Note Event in ExUI for all States.
 * This will enable users to add Note(s) for a Case.
 */
@Slf4j
@Component
public class CaseworkerCaseNote implements CCDConfig<CaseData, State, UserRole> {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private IdamService idamService;

    @Autowired
    private Clock clock;

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
        return new PageBuilder(configBuilder
                                   .event(CASEWORKER_ADD_CASE_NOTE)
                                   .forAllStates()
                                   .name(ADD_CASE_NOTE)
                                   .description(ADD_CASE_NOTE)
                                   .showSummary()
                                   .aboutToSubmitCallback(this::aboutToSubmit));
    }

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(
        final CaseDetails<CaseData, State> details,
        final CaseDetails<CaseData, State> beforeDetails
    ) {
        log.info("Caseworker add notes callback invoked for Case Id: {}", details.getId());

        final User caseworkerUser = idamService.retrieveUser(request.getHeader(AUTHORIZATION));

        var caseData = details.getData();
        CaseNote caseNote = caseData.getNote();
        caseNote.setDate(LocalDate.now(clock));
        caseNote.setUser(caseworkerUser.getUserDetails().getFullName());

        if (isEmpty(caseData.getCaseNote())) {
            List<ListValue<CaseNote>> listValues = new ArrayList<>();

            var listValue = ListValue
                .<CaseNote>builder()
                .id("1")
                .value(caseNote)
                .build();

            listValues.add(listValue);

            caseData.setCaseNote(listValues);
        } else {
            AtomicInteger listValueIndex = new AtomicInteger(0);
            var listValue = ListValue
                .<CaseNote>builder()
                .value(caseNote)
                .build();

            caseData.getCaseNote().add(0, listValue); // always add new note as first element so that it is displayed on top

            caseData.getCaseNote().forEach(caseNoteListValue -> caseNoteListValue.setId(String.valueOf(listValueIndex.incrementAndGet())));
        }

        caseData.setNote(null); //Clear note text area as notes value is stored in notes collection

        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .build();
    }

}
