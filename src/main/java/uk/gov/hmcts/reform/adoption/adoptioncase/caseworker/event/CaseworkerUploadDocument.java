package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.ManageDocuments;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

/**
 * This class was used to define the Manage Case Event
 * It remains because EXUI needs at least one event (with permissions granted) so that FieldTypes of Label are
 * included on the AuthorisationCaseField tab of the ccd-config Excel file.  EXUI-1308 raised for attention.
 */

@Component
@Slf4j
public class CaseworkerUploadDocument implements CCDConfig<CaseData, State, UserRole> {

    public static final String CASEWORKER_UPLOAD_DOCUMENT = "caseworker-manage-document";
    public static final String MANAGE_DOCUMENT = "Manage documents";

    private final CcdPageConfiguration manageDocuments = new ManageDocuments();

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        var pageBuilder = addEventConfig(configBuilder);
        manageDocuments.addTo(pageBuilder);
    }

    private PageBuilder addEventConfig(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.grant(State.Draft, Permissions.READ_UPDATE, UserRole.CASE_WORKER);
        return new PageBuilder(configBuilder
                                   .event(CASEWORKER_UPLOAD_DOCUMENT)
                                   .forAllStates()
                                   .name(MANAGE_DOCUMENT)
                                   .description(MANAGE_DOCUMENT)
                                   .showSummary()
                                   .grant(Permissions.CREATE_READ_UPDATE, UserRole.CASE_WORKER)
                                   .grant(Permissions.CREATE_READ_UPDATE, UserRole.DISTRICT_JUDGE));
    }
}
