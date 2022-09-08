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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * This class is used to define the Manage Case Event
 * This will enable the upload document functionality.
 * It will also would allow user to specify the Category of document
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
                                   .aboutToSubmitCallback(this::aboutToSubmit)
                                   .showSummary(false)
                                   .grant(Permissions.CREATE_READ_UPDATE, UserRole.CASE_WORKER));
    }

    /**
     * This method is used to segregate all the documents post submission into specific pre-defined categories.
     */
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
        switch (caseData.getAdoptionDocument().getDocumentCategory()) {
            case APPLICATION_DOCUMENTS -> {
                caseData.setApplicationDocumentsCategory(addDocumentToListOfSpecificCategory(
                    caseData,
                    caseData.getApplicationDocumentsCategory()
                ));
                break;
            }
            case COURT_ORDERS -> {
                caseData.setCourtOrdersDocumentCategory(addDocumentToListOfSpecificCategory(
                    caseData,
                    caseData.getCourtOrdersDocumentCategory()
                ));
                break;
            }
            case REPORTS -> {
                caseData.setReportsDocumentCategory(addDocumentToListOfSpecificCategory(
                    caseData,
                    caseData.getReportsDocumentCategory()
                ));
                break;
            }
            case STATEMENTS -> {
                caseData.setStatementsDocumentCategory(addDocumentToListOfSpecificCategory(
                    caseData,
                    caseData.getStatementsDocumentCategory()
                ));
                break;
            }
            case CORRESPONDENCE -> {
                caseData.setCorrespondenceDocumentCategory(addDocumentToListOfSpecificCategory(
                    caseData,
                    caseData.getCorrespondenceDocumentCategory()
                ));
                break;
            }
            case ADDITIONAL_DOCUMENTS -> {
                caseData.setAdditionalDocumentsCategory(addDocumentToListOfSpecificCategory(
                    caseData,
                    caseData.getAdditionalDocumentsCategory()
                ));
                break;
            }
            default -> log.info("Document doesn't fall under any provided category");
        }

        log.info("-----------CaseData {}",caseData);
        if (caseData.getAdoptionDocument().getOtherPartyName() != null || caseData.getAdoptionDocument().getOtherPartyRole() != null) {
            List<String> errors = new ArrayList<>();
            errors.add("Test Error");
            return AboutToStartOrSubmitResponse.<CaseData, State>builder()
                .errors(errors)
                .build();
        }



        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .build();
    }

    /**
     * This method will add the adoption document to list at the top of the list.
     * Based on whether the list was empty or already had other documents present.
     */
    private List<ListValue<AdoptionDocument>> addDocumentToListOfSpecificCategory(CaseData caseData,
                                                                                  List<ListValue<AdoptionDocument>> adoptionDocumentList) {

        if (isEmpty(adoptionDocumentList)) {
            List<ListValue<AdoptionDocument>> listValues = new ArrayList<>();

            var listValue = ListValue
                .<AdoptionDocument>builder()
                .id("1")
                .value(caseData.getAdoptionDocument())
                .build();

            listValues.add(listValue);
            caseData.setAdoptionDocument(null);
            return listValues;
        } else {
            AtomicInteger listValueIndex = new AtomicInteger(0);
            var listValue = ListValue
                .<AdoptionDocument>builder()
                .value(caseData.getAdoptionDocument())
                .build();
            // always add new Adoption Document as first element so that it is displayed on top
            adoptionDocumentList.add(
                0,
                listValue
            );

            adoptionDocumentList.forEach(adoptionDocumentListValue
                                             -> adoptionDocumentListValue.setId(String.valueOf(listValueIndex.incrementAndGet())));
        }
        //Clear adoption document so that value doesn't persist while navigating to same screen subsequently
        caseData.setAdoptionDocument(null);
        return adoptionDocumentList;
    }
}

