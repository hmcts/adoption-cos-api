package uk.gov.hmcts.reform.adoption.adoptioncase.event;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.ConfigureTtlFields;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.CASE_WORKER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SYSTEM_UPDATE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.TTL_PROFILE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions.CREATE_READ_UPDATE;

@Slf4j
@Component
public class ManageCaseTtl implements CCDConfig<CaseData, State, UserRole> {
    public static final String MANAGE_CASE_TTL = "manageCaseTTL";

    private final CcdPageConfiguration configureTtl = new ConfigureTtlFields();

    @Override
    public void configure(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        PageBuilder pageBuilder = new PageBuilder(configBuilder
                                                      .event(MANAGE_CASE_TTL)
                                                      .forAllStates()
                                                      .name("Manage Case TTL")
                                                      .description("Adoption application update Retain & Dispose Time To Live")
                                                      .retries(120, 120)
                                                      .grant(CREATE_READ_UPDATE, TTL_PROFILE)
                                                      .showSummary()

        );
        configureTtl.addTo(pageBuilder);
    }
}
