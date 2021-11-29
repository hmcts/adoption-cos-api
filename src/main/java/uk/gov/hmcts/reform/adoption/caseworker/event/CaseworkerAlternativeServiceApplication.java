package uk.gov.hmcts.reform.adoption.caseworker.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.divorcecase.model.AlternativeService;
import uk.gov.hmcts.reform.adoption.divorcecase.model.CaseData;
import uk.gov.hmcts.reform.adoption.divorcecase.model.State;
import uk.gov.hmcts.reform.adoption.divorcecase.model.UserRole;

import java.time.Clock;
import java.time.LocalDate;

import static uk.gov.hmcts.reform.adoption.divorcecase.model.State.AosDrafted;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.State.AosOverdue;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.State.AwaitingAos;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.State.AwaitingDocuments;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.State.AwaitingServicePayment;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.State.Submitted;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.UserRole.CASE_WORKER;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.UserRole.CITIZEN;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.UserRole.LEGAL_ADVISOR;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.UserRole.SOLICITOR;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.UserRole.SUPER_USER;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.access.Permissions.CREATE_READ_UPDATE;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.access.Permissions.READ;

@Component
@Slf4j
public class CaseworkerAlternativeServiceApplication implements CCDConfig<CaseData, State, UserRole> {
    public static final String CASEWORKER_SERVICE_RECEIVED = "caseworker-service-received";

    @Autowired
    private Clock clock;

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        new PageBuilder(configBuilder
            .event(CASEWORKER_SERVICE_RECEIVED)
            .forStates(AosOverdue, AwaitingAos, AosDrafted, Submitted, AwaitingDocuments)
            .name("Service application received")
            .description("Service application received")
            .explicitGrants()
            .showSummary()
            .aboutToSubmitCallback(this::aboutToSubmit)
            .grant(CREATE_READ_UPDATE, CASE_WORKER)
            .grant(READ, SUPER_USER, LEGAL_ADVISOR, SOLICITOR, CITIZEN))
            .page("serviceApplicationReceived")
            .pageLabel("Service application received")
            .complex(CaseData::getAlternativeService)
                .mandatory(AlternativeService::getReceivedServiceApplicationDate)
                .mandatory(AlternativeService::getAlternativeServiceType)
                .done();
    }

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(
        final CaseDetails<CaseData, State> details,
        final CaseDetails<CaseData, State> beforeDetails
    ) {
        log.info("Caseworker create service application about to submit callback invoked");

        var caseData = details.getData();

        caseData.getAlternativeService().setReceivedServiceAddedDate(LocalDate.now(clock));

        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .state(AwaitingServicePayment)
            .build();
    }
}
