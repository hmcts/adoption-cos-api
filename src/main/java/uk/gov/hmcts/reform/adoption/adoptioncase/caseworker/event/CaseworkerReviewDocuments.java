package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.ReviewDocuments;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionDocument;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionUploadDocument;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.idam.client.models.User;

import javax.servlet.http.HttpServletRequest;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
@Slf4j
public class CaseworkerReviewDocuments implements CCDConfig<CaseData, State, UserRole> {

    @Autowired
    private Clock clock;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private IdamService idamService;

    public static final String CASEWORKER_REVIEW_DOCUMENT = "caseworker-review-document";
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
                                   .event(CASEWORKER_REVIEW_DOCUMENT)
                                   .forAllStates()
                                   .name(SCANNED_DOCUMENT)
                                   .description(SCANNED_DOCUMENT)
                                   .showSummary()
                                   .aboutToSubmitCallback(this::aboutToSubmit)
                                   .grant(Permissions.CREATE_READ_UPDATE, UserRole.CASE_WORKER));
    }

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(
        final CaseDetails<CaseData, State> details,
        final CaseDetails<CaseData, State> beforeDetails
    ) {
        log.info("Callback invoked for {}", CASEWORKER_REVIEW_DOCUMENT);

        var caseData = details.getData();
        final User caseworkerUser = idamService.retrieveUser(request.getHeader(AUTHORIZATION));

        /*caseData.getLaDocumentsUploaded().stream()
                .forEach(laUploadedDocument -> {
                    laUploadedDocument.getValue().setDate(LocalDate.now(clock));
                    laUploadedDocument.getValue().setUser(caseworkerUser.getUserDetails().getFullName());
                    switch (laUploadedDocument.getValue().getDocumentCategory()) {
                        case APPLICATION_DOCUMENTS -> {
                            caseData.setApplicationDocumentsCategory(addDocumentToListOfSpecificCategory(
                                caseData,
                                caseData.getApplicationDocumentsCategory(),
                                laUploadedDocument.getValue()
                            ));
                            break;
                        }
                        case COURT_ORDERS -> {
                            caseData.setCourtOrdersDocumentCategory(addDocumentToListOfSpecificCategory(
                                caseData,
                                caseData.getCourtOrdersDocumentCategory(),
                                laUploadedDocument.getValue()
                            ));
                            break;
                        }
                        case REPORTS -> {
                            caseData.setReportsDocumentCategory(addDocumentToListOfSpecificCategory(
                                caseData,
                                caseData.getReportsDocumentCategory(),
                                laUploadedDocument.getValue()
                            ));
                            break;
                        }
                        case STATEMENTS -> {
                            caseData.setStatementsDocumentCategory(addDocumentToListOfSpecificCategory(
                                caseData,
                                caseData.getStatementsDocumentCategory(),
                                laUploadedDocument.getValue()
                            ));
                            break;
                        }
                        case CORRESPONDENCE -> {
                            caseData.setCorrespondenceDocumentCategory(addDocumentToListOfSpecificCategory(
                                caseData,
                                caseData.getCorrespondenceDocumentCategory(),
                                laUploadedDocument.getValue()
                            ));
                            break;
                        }
                        case ADDITIONAL_DOCUMENTS -> {
                            caseData.setAdditionalDocumentsCategory(addDocumentToListOfSpecificCategory(
                                caseData,
                                caseData.getAdditionalDocumentsCategory(),
                                laUploadedDocument.getValue()
                            ));
                            break;
                        }

                        default -> log.info("Document doesn't fall under any provided category");
                    }

                });*/
        //Clear adoption document so that it's removed from the Documents tab
        caseData.setLaDocumentsUploaded(null);

        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .build();
    }

    /**
     * This method will add the adoption document to list at the top of the list.
     * Based on whether the list was empty or already had other documents present.
     */
    private List<ListValue<AdoptionUploadDocument>> addDocumentToListOfSpecificCategory(
        CaseData caseData, List<ListValue<AdoptionUploadDocument>> adoptionDocumentList, AdoptionDocument adoptionDocument) {
        log.info("Callback invoked for {}", CASEWORKER_REVIEW_DOCUMENT);
        AdoptionUploadDocument adoptionUploadDocument = new AdoptionUploadDocument();
        adoptionUploadDocument.setDocumentDateAdded(adoptionDocument.getDocumentDateAdded());
        //adoptionUploadDocument.setDocumentCategory(adoptionDocument.getDocumentCategory());
        adoptionUploadDocument.setDocumentComment(adoptionDocument.getDocumentComment());
        adoptionUploadDocument.setDocumentLink(adoptionDocument.getDocumentLink());
        //adoptionUploadDocument.setName(adoptionDocument.getName());
        //adoptionUploadDocument.setRole(adoptionDocument.getRole());

        if (isEmpty(adoptionDocumentList)) {
            List<ListValue<AdoptionUploadDocument>> listValues = new ArrayList<>();

            var listValue = ListValue
                .<AdoptionUploadDocument>builder()
                .id("1")
                .value(adoptionUploadDocument)
                .build();

            listValues.add(listValue);
            return listValues;
        } else {
            AtomicInteger listValueIndex = new AtomicInteger(0);
            var listValue = ListValue
                .<AdoptionUploadDocument>builder()
                .value(adoptionUploadDocument)
                .build();
            // always add new Adoption Document as first element so that it is displayed on top
            adoptionDocumentList.add(
                0,
                listValue
            );

            adoptionDocumentList.forEach(adoptionDocumentListValue
                                             -> adoptionDocumentListValue.setId(String.valueOf(listValueIndex.incrementAndGet())));
        }

        return adoptionDocumentList;
    }
}
