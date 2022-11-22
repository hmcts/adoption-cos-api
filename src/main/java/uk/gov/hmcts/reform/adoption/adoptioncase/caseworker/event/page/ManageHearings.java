package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageHearingDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.validation.RecipientValidationUtil;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.document.CaseDataDocumentService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.BLANK_SPACE;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.FINAL_ADOPTION_ORDER_A76;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.FINAL_ADOPTION_ORDER_A76_FILE_NAME;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.MANAGE_HEARING_NOTICES_A90;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.MANAGE_HEARING_NOTICES_A90_FILE_NAME;
import static uk.gov.hmcts.reform.adoption.document.DocumentUtil.formatDocumentName;

/**
 * Contains method to add Page Configuration for ExUI.
 * Display the Manage hearings Details screen with all required fields.
 */
@Slf4j
@Component
public class ManageHearings implements CcdPageConfiguration {

    @Autowired
    private CaseDataDocumentService caseDataDocumentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder.page("manageHearing1", this::midEvent)
            .mandatory(CaseData::getManageHearingOptions)
            .page("manageHearing2")
            .showCondition("manageHearingOptions=\"vacateHearing\"")
            .label("vacateHearingLabel1","## Vacate a hearing", "manageHearingOptions=\"vacateHearing\"")
            .mandatory(CaseData::getHearingListThatCanBeVacated,"manageHearingOptions=\"vacateHearing\"")
            .page("manageHearing2.1")
            .showCondition("manageHearingOptions= \"adjournHearing\"")
            .label("vacateHearingLabel2","## Adjourn a hearing", "manageHearingOptions=\"adjournHearing\"")
            .mandatory(CaseData::getHearingListThatCanBeAdjourned)
            .page("manageHearing3")
            .showCondition("manageHearingOptions=\"vacateHearing\"")
            .mandatory(CaseData::getReasonForVacatingHearing)
            .page("manageHearing31")
            .showCondition("manageHearingOptions=\"adjournHearing\"")
            .mandatory(CaseData::getReasonForAdjournHearing)
            .page("manageHearing4")
            .showCondition("manageHearingOptions=\"vacateHearing\" OR manageHearingOptions=\"adjournHearing\"")
            .label("relistingLabel1","## Relisting")
            .mandatory(CaseData::getIsTheHearingNeedsRelisting)
            .page("manageHearing5")
            .showCondition("manageHearingOptions=\"addNewHearing\" OR isTheHearingNeedsRelisting=\"Yes\"")
            .label("addNewHearing2", "## Add new hearing")
            .complex(CaseData::getManageHearingDetails)
            .mandatory(ManageHearingDetails::getTypeOfHearing)
            .mandatory(ManageHearingDetails::getHearingDateAndTime)
            .mandatory(ManageHearingDetails::getLengthOfHearing)
            .mandatory(ManageHearingDetails::getJudge)
            .mandatory(ManageHearingDetails::getCourt)
            .optional(ManageHearingDetails::getIsInterpreterNeeded)
            .mandatory(ManageHearingDetails::getMethodOfHearing)
            .optional(ManageHearingDetails::getAccessibilityRequirements)
            .optional(ManageHearingDetails::getHearingDirections)
            .done()
            .page("manageHearing6",this::midEventAfterRecipientSelection)
            .showCondition("manageHearingOptions=\"addNewHearing\" OR isTheHearingNeedsRelisting=\"Yes\"")
            .mandatory(CaseData::getRecipientsInTheCase)
            .page("manageHearing7")
            .showCondition("recipientsInTheCase=\"applicant1\" OR recipientsInTheCase=\"applicant2\"")
            .label("manageHearing71","Preview Draft",null, true)
            .optional(CaseData::getHearingA90Document)
            .done()
            .build();
    }

    public AboutToStartOrSubmitResponse<CaseData, State> midEvent(CaseDetails<CaseData, State> details,
                                                                   CaseDetails<CaseData, State> detailsBefore) {
        CaseData caseData = details.getData();
        List<DynamicListElement> listElements = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(caseData.getNewHearings())) {
            caseData.getNewHearings().forEach(hearing -> {
                DynamicListElement listElement = DynamicListElement.builder()
                    .label(String.join(BLANK_SPACE, hearing.getValue().getTypeOfHearing(),
                                       "-",
                                       hearing.getValue().getHearingDateAndTime().format(DateTimeFormatter.ofPattern(
                                           "dd MMM yyyy',' hh:mm:ss a")).replace("pm", "PM").replace("am", "PM")
                    )).code(UUID.fromString(hearing.getValue().getHearingId()))
                    .build();
                listElements.add(listElement);
            });

        }
        caseData.setHearingListThatCanBeVacated(DynamicList.builder().listItems(listElements).value(DynamicListElement.EMPTY).build());
        caseData.setHearingListThatCanBeAdjourned(DynamicList.builder().listItems(listElements).value(DynamicListElement.EMPTY).build());
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .build();
    }


    /*
    This MidEvent will validate if any incorrect selection of Recipients is made.
    In case any non-applicable Recipient is selected
    System will throw an error.
     */
    public AboutToStartOrSubmitResponse<CaseData, State> midEventAfterRecipientSelection(
        CaseDetails<CaseData, State> details,
        CaseDetails<CaseData, State> detailsBefore
    ) {
        var caseData = details.getData();
        List<String> error = new ArrayList<>();

        RecipientValidationUtil.checkingApplicantRelatedSelectedRecipients(caseData, error);
        RecipientValidationUtil.checkingChildRelatedSelectedRecipient(caseData, error);
        RecipientValidationUtil.checkingParentRelatedSelectedRecipients(caseData, error);
        RecipientValidationUtil.checkingOtherPersonRelatedSelectedRecipients(caseData, error);
        RecipientValidationUtil.checkingAdoptionAgencyRelatedSelectedRecipients(caseData, error);

        if (isEmpty(error)) {
            @SuppressWarnings("unchecked")
            Map<String, Object> templateContent = objectMapper.convertValue(caseData, Map.class);
            log.info("templateContent {}", templateContent);
            caseData.getManageHearingDetails().setHearingA90Document(caseDataDocumentService.renderDocument(
                templateContent,
                details.getId(),
                MANAGE_HEARING_NOTICES_A90,
                LanguagePreference.ENGLISH,
                formatDocumentName(
                    details.getId(),
                    MANAGE_HEARING_NOTICES_A90_FILE_NAME,
                    LocalDateTime.now()
                )));
            //TODO:  Set this value as null in aboutToSubmit callback
            caseData.setHearingA90Document(caseData.getManageHearingDetails().getHearingA90Document());
        }

        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .errors(error)
            .build();
    }




}
