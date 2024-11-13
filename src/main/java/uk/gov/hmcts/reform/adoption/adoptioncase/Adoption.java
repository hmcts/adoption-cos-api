package uk.gov.hmcts.reform.adoption.adoptioncase;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Draft;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.LaSubmitted;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Submitted;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.CITIZEN;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SYSTEM_UPDATE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions.CREATE_READ_UPDATE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AwaitingPayment;


@Component
public class Adoption implements CCDConfig<CaseData, State, UserRole> {
    public static final String CASE_TYPE = "A58";
    public static final String JURISDICTION = "ADOPTION";

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.setCallbackHost(System.getenv().getOrDefault("CASE_API_URL", "http://localhost:4550"));
        configBuilder.caseType(CASE_TYPE, "New Adoption case", "New Adoption case");
        configBuilder.jurisdiction(JURISDICTION, "Adoption", "Child adoption");

        configBuilder.grant(Draft, CREATE_READ_UPDATE, CITIZEN);
        configBuilder.grant(Draft, CREATE_READ_UPDATE, SYSTEM_UPDATE);
        configBuilder.grant(AwaitingPayment, CREATE_READ_UPDATE, SYSTEM_UPDATE);
        configBuilder.grant(Submitted, CREATE_READ_UPDATE, SYSTEM_UPDATE);
        configBuilder.grant(LaSubmitted, CREATE_READ_UPDATE, SYSTEM_UPDATE);
    }
}
