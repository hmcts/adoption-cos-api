package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.ManageDocuments;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionDocument;

import java.util.UUID;


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
                            .aboutToSubmitCallback(this::aboutToSubmit)
                            .showSummary(false)
                            .grant(Permissions.CREATE_READ_UPDATE, UserRole.CASE_WORKER));
    }

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(
        final CaseDetails<CaseData, State> details,
        final CaseDetails<CaseData, State> beforeDetails
    ) {
        log.info("Callback invoked for {}", CASEWORKER_UPLOAD_DOCUMENT);

        var caseData = details.getData();
        ListValue<AdoptionDocument> adoptionDocument = ListValue.<AdoptionDocument>builder()
            .id(String.valueOf(UUID.randomUUID()))
            .value(caseData.getAdoptionDocument())
            .build();
        caseData.addToDocumentsUploaded(adoptionDocument);
        caseData.sortUploadedDocuments(beforeDetails.getData().getDocumentsUploaded());

        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .build();
    }
}

