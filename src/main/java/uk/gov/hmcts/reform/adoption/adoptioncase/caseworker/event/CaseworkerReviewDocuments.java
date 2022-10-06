package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.ReviewDocuments;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

@Component
@Slf4j
public class CaseworkerReviewDocuments implements CCDConfig<CaseData, State, UserRole> {

    public static final String CASEWORKER_SCANNED_DOCUMENT = "caseworker-review-document";
    public static final String SCANNED_DOCUMENT = "Review all documents";

    private final CcdPageConfiguration manageDocuments = new ReviewDocuments();

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
