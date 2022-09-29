package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.ScannedDocuments;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

@Component
@Slf4j
public class CaseworkerScannedDocuments implements CCDConfig<CaseData, State, UserRole> {

    public static final String CASEWORKER_SCANNED_DOCUMENT = "caseworker-scanned-document";
    public static final String SCANNED_DOCUMENT = "Scanned documents";

    private final CcdPageConfiguration manageDocuments = new ScannedDocuments();

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        var pageBuilder = addEventConfig(configBuilder);
        manageDocuments.addTo(pageBuilder);
    }

    private PageBuilder addEventConfig(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.grant(State.Draft, Permissions.READ_UPDATE, UserRole.CASE_WORKER);
        return new PageBuilder(configBuilder
                                   .event(CASEWORKER_SCANNED_DOCUMENT)
                                   .forAllStates()
                                   .name(SCANNED_DOCUMENT)
                                   .description(SCANNED_DOCUMENT)
                                   .showSummary()
                                   //.aboutToSubmitCallback(this::aboutToSubmit)
                                   .grant(Permissions.CREATE_READ_UPDATE, UserRole.CASE_WORKER));
    }
}
