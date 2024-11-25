package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.AmendOtherPartiesDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Parent;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.YesNoNotSure;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

@Component
@Slf4j
public class CaseworkerAmendOtherPartiesDetails implements CCDConfig<CaseData, State, UserRole> {
    public static final String CASEWORKER_AMEND_OTHER_PARTIES_DETAILS = "caseworker-amend-other-parties-details";
    public static final String AMEND_OTHER_PARTIES_DETAILS = "Amend other parties details";

    private final CcdPageConfiguration amendOtherPartiesDetails = new AmendOtherPartiesDetails();



    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        log.info("Inside configure method for Event {}", CASEWORKER_AMEND_OTHER_PARTIES_DETAILS);
        var pageBuilder = addEventConfig(configBuilder);
        amendOtherPartiesDetails.addTo(pageBuilder);
    }

    public PageBuilder addEventConfig(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        return new PageBuilder(configBuilder
                                   .event(CASEWORKER_AMEND_OTHER_PARTIES_DETAILS)
                                   .forStates(State.LaSubmitted)
                                   .name(AMEND_OTHER_PARTIES_DETAILS)
                                   .description(AMEND_OTHER_PARTIES_DETAILS)
                                   .showSummary()
                                   .grant(Permissions.CREATE_READ_UPDATE, UserRole.CASE_WORKER)
                                   .grant(Permissions.CREATE_READ_UPDATE, UserRole.DISTRICT_JUDGE)
                                   .aboutToSubmitCallback(this::aboutToSubmit));
    }


    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(
        final CaseDetails<CaseData, State> details,
        final CaseDetails<CaseData, State> beforeDetails
    ) {
        log.info("Callback invoked for {}", CASEWORKER_AMEND_OTHER_PARTIES_DETAILS);

        var caseData = details.getData();
        Parent birthMother = caseData.getBirthMother();
        birthMother.setStillAlive(YesNoNotSure.YES.equals(birthMother.getDeceased().getValue()) ? YesNoNotSure.NO : YesNoNotSure.YES);
        Parent birthFather = caseData.getBirthFather();
        birthFather.setStillAlive(YesNoNotSure.YES.equals(birthMother.getDeceased().getValue()) ? YesNoNotSure.NO : YesNoNotSure.YES);
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .build();
    }
}

