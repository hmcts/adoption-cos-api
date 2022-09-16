package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import uk.gov.hmcts.reform.adoption.document.model.AdoptionUploadDocument;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    private Clock clock;

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
                                   .aboutToSubmitCallback(this::aboutToSubmit)
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

        caseData.getAdoptionUploadDocument().setName(caseData.getName());
        caseData.getAdoptionUploadDocument().setRole(caseData.getRole());

        switch (caseData.getAdoptionUploadDocument().getDocumentCategory()) {
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

        caseData.setRole(null);
        caseData.setName(null);
        log.info("Set Role and Name as null");
        log.info("-----------CaseData {}",caseData);

        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .build();
    }

    /**
     * This method will add the adoption document to list at the top of the list.
     * Based on whether the list was empty or already had other documents present.
     */
    private List<ListValue<AdoptionUploadDocument>> addDocumentToListOfSpecificCategory(
        CaseData caseData, List<ListValue<AdoptionUploadDocument>> adoptionDocumentList) {
        AdoptionUploadDocument adoptionDocument = caseData.getAdoptionUploadDocument();
        adoptionDocument.setDocumentDateAdded(LocalDate.now(clock));
        adoptionDocument.setDocumentCategory(null);

        if (isEmpty(adoptionDocumentList)) {
            List<ListValue<AdoptionUploadDocument>> listValues = new ArrayList<>();

            var listValue = ListValue
                .<AdoptionUploadDocument>builder()
                .id("1")
                .value(adoptionDocument)
                .build();

            listValues.add(listValue);
            caseData.setAdoptionUploadDocument(null);
            return listValues;
        } else {
            AtomicInteger listValueIndex = new AtomicInteger(0);
            var listValue = ListValue
                .<AdoptionUploadDocument>builder()
                .value(adoptionDocument)
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
        caseData.setAdoptionUploadDocument(null);
        return adoptionDocumentList;
    }
}
