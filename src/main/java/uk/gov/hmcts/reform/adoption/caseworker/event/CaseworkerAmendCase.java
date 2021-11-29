package uk.gov.hmcts.reform.adoption.caseworker.event;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.caseworker.event.page.AmendCase;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AosDrafted;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AosOverdue;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AwaitingAos;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AwaitingClarification;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AwaitingConditionalOrder;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AwaitingLegalAdvisorReferral;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AwaitingPronouncement;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.ConditionalOrderDrafted;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.ConditionalOrderPronounced;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.ConditionalOrderRefused;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Holding;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.CASE_WORKER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.LEGAL_ADVISOR;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SUPER_USER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions.CREATE_READ_UPDATE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions.READ;

@Component
public class CaseworkerAmendCase implements CCDConfig<CaseData, State, UserRole> {
    public static final String CASEWORKER_AMEND_CASE = "caseworker-amend-case";
    private final CcdPageConfiguration amendCase = new AmendCase();


    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        var pageBuilder = addEventConfig(configBuilder);
        amendCase.addTo(pageBuilder);
    }

    private PageBuilder addEventConfig(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        return new PageBuilder(configBuilder
            .event(CASEWORKER_AMEND_CASE)
            .forStates(AwaitingAos, AosDrafted, AosOverdue, Holding, AwaitingConditionalOrder,
                ConditionalOrderDrafted, AwaitingLegalAdvisorReferral, AwaitingClarification,
                ConditionalOrderRefused, AwaitingPronouncement, ConditionalOrderPronounced)
            .name("Update case")
            .description("Update case")
            .showSummary()
            .explicitGrants()
            .grant(CREATE_READ_UPDATE,
                CASE_WORKER)
            .grant(READ,
                SUPER_USER,
                LEGAL_ADVISOR));
    }
}
