package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.SeekFurtherInformation;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.document.CaseDataDocumentService;
import uk.gov.hmcts.reform.adoption.document.DocumentSubmitter;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionUploadDocument;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.idam.client.models.User;

import javax.servlet.http.HttpServletRequest;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.util.CollectionUtils.isEmpty;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.BLANK_SPACE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.STRING_COLON;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.SEEK_FURTHER_INFO_LETTER;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.SEEK_FURTHER_INFO_LETTER_FILE_NAME;

@Slf4j
@Component
public class CaseworkerSeekFurtherInformation implements CCDConfig<CaseData, State, UserRole> {

    public static final String CASEWORKER_SEEK_FURTHER_INFORMATION = "caseworker-seekfurther-information";

    public static final String SEEK_FURTHER_INFORMATION_HEADING = "Seek further information";

    private final CcdPageConfiguration seekFurtherInformation = new SeekFurtherInformation();

    @Autowired
    private Clock clock;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private IdamService idamService;

    @Autowired
    CaseDataDocumentService caseDataDocumentService;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void configure(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        log.info("Inside configure method for Event {}", CASEWORKER_SEEK_FURTHER_INFORMATION);
        var pageBuilder = addEventConfig(configBuilder);
        seekFurtherInformation.addTo(pageBuilder);
        pageBuilder.page("pageSeekFurtherInformation3", this::midEventAfterDateSelection)
            .mandatory(CaseData::getDate)
            .page("pageSeekFurtherInformation4")
            .label("seekFurtherInfo4","Preview and check the letter",null, true)
            .readonly(CaseData::getSeekFurtherInformationDocument)
            .label("seekFurtherInfo5","If you want to make changes, go back to previous screen.",null, false)
            .done()
            .build();

    }


    /**
     * Helper method to make custom changes to the CCD Config in order to add the event to respective Page Configuration.
     *
     * @param configBuilder - Base CCD Config Builder updated to add Event for Page
     * @return - PageBuilder updated to use on overridden method.
     */
    private PageBuilder addEventConfig(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.grant(State.Draft, Permissions.READ_UPDATE, UserRole.CASE_WORKER, UserRole.COURT_ADMIN,
                            UserRole.LEGAL_ADVISOR, UserRole.DISTRICT_JUDGE
        );
        return new PageBuilder(configBuilder
                                   .event(CASEWORKER_SEEK_FURTHER_INFORMATION)
                                   .forAllStates()
                                   .name(SEEK_FURTHER_INFORMATION_HEADING)
                                   .description(SEEK_FURTHER_INFORMATION_HEADING)
                                   .showSummary()
                                   .grant(Permissions.CREATE_READ_UPDATE, UserRole.CASE_WORKER)
                                   .grant(Permissions.CREATE_READ_UPDATE, UserRole.DISTRICT_JUDGE)
                                   .aboutToStartCallback(this::seekFurtherInformationData)
                                   .aboutToSubmitCallback(this::aboutToSubmit));
    }

    /**
     * Method to fetch Seek Further Information list.
     *
     * @param details is type of Case Data
     * @return will return about to submit response
     */
    public AboutToStartOrSubmitResponse<CaseData, State> seekFurtherInformationData(CaseDetails<CaseData, State> details) {
        CaseData caseData = details.getData();
        List<DynamicListElement> listElements = new ArrayList<>();

        DynamicListElement childSocialWorker = DynamicListElement.builder()
            .label(joinDynamicListLabel(DocumentSubmitter.CHILD_SOCIAL_WORKER,
             caseData.getChildSocialWorker().getSocialWorkerName()))
            .code(UUID.randomUUID())
            .build();

        listElements.add(childSocialWorker);
        log.info("childSocialWorker");

        DynamicListElement adoptionAgencyOrLocalAuthority = DynamicListElement.builder()
            .label(joinDynamicListLabel(DocumentSubmitter.ADOPTION_AGENCY_OR_LOCAL_AUTHORITY,
             caseData.getAdopAgencyOrLA().getAdopAgencyOrLaName()))
            .code(UUID.randomUUID())
            .build();

        listElements.add(adoptionAgencyOrLocalAuthority);
        log.info("adoptionAgencyOrLocalAuthority");

        if (caseData.getOtherAdoptionAgencyOrLA() != null && caseData.getOtherAdoptionAgencyOrLA().getAgencyOrLaName() != null) {
            DynamicListElement otherAdoptionAgencyOrLocalAuthority = DynamicListElement.builder()
                .label(joinDynamicListLabel(DocumentSubmitter.OTHER_ADOPTION_AGENCY_OR_LOCAL_AUTHORITY,
                 caseData.getOtherAdoptionAgencyOrLA().getAgencyOrLaName()))
                .code(UUID.randomUUID())
                .build();

            listElements.add(otherAdoptionAgencyOrLocalAuthority);
            log.info("getOtherAdopAgencyOrLA");
        }

        DynamicListElement firstApplicant = DynamicListElement.builder()
            .label(joinDynamicListLabel(DocumentSubmitter.FIRST_APPLICANT,
             String.join(caseData.getApplicant1().getFirstName(), caseData.getApplicant1().getLastName())))
            .code(UUID.randomUUID())
            .build();

        listElements.add(firstApplicant);

        if (caseData.getApplicant2() != null && caseData.getApplicant2().getFirstName() != null
            && caseData.getApplicant2().getLastName() != null) {
            DynamicListElement secondApplicant = DynamicListElement.builder()
                .label(joinDynamicListLabel(DocumentSubmitter.SECOND_APPLICANT,
                 String.join(caseData.getApplicant2().getFirstName(), caseData.getApplicant2().getLastName())))
                .code(UUID.randomUUID())
                .build();

            listElements.add(secondApplicant);
        }

        caseData.setSeekFurtherInformationList(
            DynamicList.builder().listItems(listElements).value(DynamicListElement.EMPTY).build());
        log.info("MidEvent Triggered");

        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .build();
    }

    /**
     * Method will return String after concatenation.
     *
     * @param enumLabel is typeof Document Submitter
     * @param detail is typeof label
     * @return will return String after join
     */
    private String joinDynamicListLabel(DocumentSubmitter enumLabel, String detail) {
        return String.join(BLANK_SPACE,enumLabel.getLabel(),STRING_COLON, detail);
    }

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(CaseDetails<CaseData, State> caseDataStateCaseDetails,
                                                                       CaseDetails<CaseData, State> caseDataStateCaseDetailsBefore) {
        CaseData caseData = caseDataStateCaseDetails.getData();
        caseData.setCorrespondenceDocumentCategory(addSeekInformationData(caseData,
                caseData.getCorrespondenceDocumentCategory()));

        caseData.setDate(null);
        caseData.setSeekFurtherInformationList(null);
        caseData.setFurtherInformation(null);
        caseData.setAskAQuestionText(null);
        caseData.setAskForAdditionalDocumentText(null);

        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .build();
    }

    private List<ListValue<AdoptionUploadDocument>> addSeekInformationData(CaseData caseData,
                                                    List<ListValue<AdoptionUploadDocument>> correspondanceTabList) {
        final User caseworkerUser = idamService.retrieveUser(request.getHeader(AUTHORIZATION));
        var adoptionUploadDocument = new AdoptionUploadDocument();
        adoptionUploadDocument.setDocumentComment(SEEK_FURTHER_INFORMATION_HEADING);
        adoptionUploadDocument.setDocumentLink(null);
        adoptionUploadDocument.setDocumentLink(caseData.getSeekFurtherInformationDocument());
        adoptionUploadDocument.setDocumentDateAdded(LocalDate.now(clock));
        if (!Objects.isNull(caseData.getSeekFurtherInformationList())) {
            adoptionUploadDocument.setUploadedBy(caseworkerUser.getUserDetails().getFullName());
        }
        if (isEmpty(correspondanceTabList)) {
            List<ListValue<AdoptionUploadDocument>> listValues = new ArrayList<>();
            var listValue = ListValue
                .<AdoptionUploadDocument>builder().id("1").value(adoptionUploadDocument).build();
            listValues.add(listValue);
            return listValues;
        } else {
            AtomicInteger listValueIndex = new AtomicInteger(0);
            var listValue = ListValue
                .<AdoptionUploadDocument>builder()
                .value(adoptionUploadDocument)
                .build();
            correspondanceTabList.add(
                0,
                listValue
            );

            correspondanceTabList.forEach(adoptionDocumentListValue ->
                                                adoptionDocumentListValue.setId(
                                                    String.valueOf(listValueIndex.incrementAndGet())));
        }
        return correspondanceTabList;
    }

    public AboutToStartOrSubmitResponse<CaseData, State> midEventAfterDateSelection(
        CaseDetails<CaseData, State> details,
        CaseDetails<CaseData, State> detailsBefore
    ) {
        var caseData = details.getData();
        caseData.setSeekFurtherInformationDocumentSubmitterName(caseData.getSeekFurtherInformationList()
                                                                    .getValue().getLabel().split(STRING_COLON)[1]);
        if (caseData.getSeekFurtherInformationList().getValue().getLabel().contains(DocumentSubmitter
                                                                                        .ADOPTION_AGENCY_OR_LOCAL_AUTHORITY.getLabel())) {
            caseData.setSeekFurtherInformationAdopOrLaSelected(YesOrNo.YES);
        } else {
            caseData.setSeekFurtherInformationAdopOrLaSelected(YesOrNo.NO);
        }
        List<String> error = new ArrayList<>();

        if (ObjectUtils.isEmpty(error)) {
            @SuppressWarnings("unchecked")
            Map<String, Object> templateContent = objectMapper.convertValue(caseData, Map.class);
            caseData.setSeekFurtherInformationDocument(caseDataDocumentService.renderDocument(
                templateContent,
                details.getId(),
                SEEK_FURTHER_INFO_LETTER,
                LanguagePreference.ENGLISH,
                SEEK_FURTHER_INFO_LETTER_FILE_NAME));
        }

        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .errors(error)
            .build();
    }
}
