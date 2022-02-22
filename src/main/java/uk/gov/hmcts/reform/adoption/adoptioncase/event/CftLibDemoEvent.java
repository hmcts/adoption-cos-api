package uk.gov.hmcts.reform.adoption.adoptioncase.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
/*import uk.gov.hmcts.ccd.sdk.api.CaseDetails;*/
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
/*import uk.gov.hmcts.ccd.sdk.api.Permission;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Applicant;*/
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

/*import java.time.LocalDate;
import java.util.List;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Draft;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.ADOPTION_GENERIC;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions.CREATE_READ_UPDATE;*/

//This is the test class created to test cftlib.
@Component
@Slf4j
public class CftLibDemoEvent implements CCDConfig<CaseData, State, UserRole> {

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        /*configBuilder.grant(Draft, Permission.CRU, ADOPTION_GENERIC);

        configBuilder.tab("demo", "Demo")
            .forRoles(ADOPTION_GENERIC)
            .field(CaseData::getDateChildMovedIn);

        configBuilder
            .event("cftlib-demo")
            .initialState(Draft)
            .name("Demo event")
            .description("Apply for adoption")
            .showSummary()
            .grant(CREATE_READ_UPDATE, ADOPTION_GENERIC)
            .retries(120, 120)
            .fields()
            .page("Details", this::midEvent)
            .mandatory(CaseData::getDateChildMovedIn)
            .complex(CaseData::getApplicant1)
            .mandatory(Applicant::getLastName)
            .mandatory(Applicant::getAddress2);*/
    }

    /*private AboutToStartOrSubmitResponse<CaseData, State> midEvent(
        CaseDetails<CaseData, State> details,
        CaseDetails<CaseData, State> detailsBefore) {
        var builder = AboutToStartOrSubmitResponse.<CaseData, State>builder();
        builder.data(details.getData());
        if (details.getData().getDateChildMovedIn().isBefore(LocalDate.parse("1900-01-01"))) {
            builder.errors(List.of("Date must be after 1900"));
        }
        return builder.build();
    }*/
}
