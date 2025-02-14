package uk.gov.hmcts.reform.adoption.adoptioncase.event;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Submitted;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SUPER_USER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SYSTEM_UPDATE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions.CREATE_READ_UPDATE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions.READ;

@Component
public class CaseworkerUpdateDssApplication implements CCDConfig<CaseData, State, UserRole> {

    public static final String CASEWORKER_UPDATE = "caseworker-update-dss-application";

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {

        configBuilder
            .event(CASEWORKER_UPDATE)
            .forStates(Submitted)
            .showCondition("applicant1Email=\"DO_NOT_SHOW\"")
            .name("Adoption case")
            .description("Adoption application update")
            .retries(120, 120)
            .grant(CREATE_READ_UPDATE, SYSTEM_UPDATE)
            .grant(READ, SUPER_USER);
    }
}
