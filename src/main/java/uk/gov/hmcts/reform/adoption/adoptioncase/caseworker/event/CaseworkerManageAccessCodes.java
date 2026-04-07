package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.ManageAccessCodes;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions.CREATE_READ_UPDATE;

public class CaseworkerManageAccessCodes implements CCDConfig<CaseData, State, UserRole> {
    public static final String MANAGE_ACCESS_CODES = "manageAccessCodes";

    private final CcdPageConfiguration manageAccessCodes = new ManageAccessCodes();

    @Override
    public void configure(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        PageBuilder pageBuilder = new PageBuilder(configBuilder
                                                      .event(MANAGE_ACCESS_CODES)
                                                      .forStates(State.Submitted)
                                                      .name("Manage Access Codes")
                                                      .description("Manage Social Worker access codes for this case")
                                                      .retries(120, 120)
                                                      .grant(CREATE_READ_UPDATE, UserRole.SUPER_USER)
                                                      .showSummary()

        );
        manageAccessCodes.addTo(pageBuilder);
    }


}
