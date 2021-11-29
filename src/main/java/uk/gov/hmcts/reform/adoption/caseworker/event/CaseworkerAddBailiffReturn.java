package uk.gov.hmcts.reform.adoption.caseworker.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.AlternativeService;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Bailiff;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

import static uk.gov.hmcts.ccd.sdk.type.YesOrNo.YES;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AosDrafted;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AosOverdue;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AwaitingAos;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AwaitingDocuments;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AwaitingPayment;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Holding;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.IssuedToBailiff;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Submitted;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.CASE_WORKER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.CITIZEN;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.LEGAL_ADVISOR;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SOLICITOR;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SUPER_USER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions.CREATE_READ_UPDATE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions.READ;

@Component
@Slf4j
public class CaseworkerAddBailiffReturn implements CCDConfig<CaseData, State, UserRole> {

    public static final String CASEWORKER_ADD_BAILIFF_RETURN = "caseworker-add-bailiff-return";

    @Value("${aos_pack.due_date_offset_days}")
    private long dueDateOffsetDays;

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        new PageBuilder(configBuilder
            .event(CASEWORKER_ADD_BAILIFF_RETURN)
            .forStates(
                IssuedToBailiff,
                AwaitingAos,
                AosOverdue,
                AosDrafted,
                Holding,
                Submitted,
                AwaitingDocuments,
                AwaitingPayment)
            .name("Add bailiff return")
            .description("Add bailiff return")
            .showSummary()
            .aboutToSubmitCallback(this::aboutToSubmit)
            .explicitGrants()
            .grant(CREATE_READ_UPDATE, CASE_WORKER)
            .grant(READ, SUPER_USER, LEGAL_ADVISOR, SOLICITOR, CITIZEN))
            .page("addBailiffReturn")
            .pageLabel("Add Bailiff Return")
            .complex(CaseData::getAlternativeService)
                .complex(AlternativeService::getBailiff)
                    .mandatory(Bailiff::getCertificateOfServiceDate)
                    .mandatory(Bailiff::getSuccessfulServedByBailiff)
                    .mandatory(Bailiff::getReasonFailureToServeByBailiff, "successfulServedByBailiff=\"No\"")
                .done();
    }

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(final CaseDetails<CaseData, State> details,
                                                                       final CaseDetails<CaseData, State> beforeDetails) {

        final Long caseId = details.getId();
        final CaseData caseData = details.getData();
        final State state;

        log.info("Caseworker add bailiff return about to submit callback invoked for case id: {}", caseId);

        if (YES == caseData.getAlternativeService().getBailiff().getSuccessfulServedByBailiff()) {
            log.info("Setting state to Holding and due date for case id: {}", caseId);
            caseData.setDueDate(caseData.getAlternativeService().getBailiff().getCertificateOfServiceDate().plusDays(dueDateOffsetDays));
            state = Holding;
        } else {
            log.info("Setting state to AwaitingAos for case id: {}", caseId);
            state = AwaitingAos;
        }

        caseData.archiveAlternativeServiceApplicationOnCompletion();

        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .state(state)
            .build();
    }
}
