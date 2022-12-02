package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.ManageHearings;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageHearingOptions;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions;
import uk.gov.hmcts.reform.adoption.adoptioncase.validation.RecipientValidationUtil;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.document.CaseDataDocumentService;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.MANAGE_HEARING_NOTICES_A90;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.MANAGE_HEARING_NOTICES_A90_FILE_NAME;

@Component
@Slf4j
public class CaseWorkerManageHearing implements CCDConfig<CaseData, State, UserRole> {

    /**
     * The constant CASEWORKER_MANAGE_HEARINGS.
     */
    public static final String CASEWORKER_MANAGE_HEARING = "caseworker-manage-hearing";

    /**
     * The constant MANAGE_HEARINGS.
     */
    public static final String MANAGE_HEARINGS = "Manage hearings";

    private final CcdPageConfiguration manageHearings = new ManageHearings();

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CaseDataDocumentService caseDataDocumentService;

    @Autowired
    private Clock clock;

    @Override
    public void configure(ConfigBuilder<CaseData, State, UserRole> configBuilder) {

        log.info("Inside configure method for Event {}", CASEWORKER_MANAGE_HEARING);
        var pageBuilder = addEventConfig(configBuilder);
        manageHearings.addTo(pageBuilder);
        pageBuilder.page("manageHearing6",this::midEventAfterRecipientSelection)
            .showCondition("manageHearingOptions=\"addNewHearing\" OR isTheHearingNeedsRelisting=\"Yes\"")
            .mandatory(CaseData::getRecipientsInTheCase)
            .page("manageHearing7")
            .showCondition("recipientsInTheCaseCONTAINS\"applicant1\" OR recipientsInTheCaseCONTAINS\"applicant2\"")
            .label("manageHearing71","Preview Draft",null, true)
            .readonly(CaseData::getHearingA90Document)
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
                                   .event(CASEWORKER_MANAGE_HEARING)
                                   .forAllStates()
                                   .name(MANAGE_HEARINGS)
                                   .description(MANAGE_HEARINGS)
                                   .showSummary()
                                   .aboutToSubmitCallback(this::aboutToSubmit)
                                   .grant(Permissions.CREATE_READ_UPDATE, UserRole.CASE_WORKER)
                                   .grant(Permissions.CREATE_READ_UPDATE, UserRole.DISTRICT_JUDGE));
    }

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(
        CaseDetails<CaseData, State> details,
        CaseDetails<CaseData, State> beforeDetails) {

        log.info("Callback invoked for {}", CASEWORKER_MANAGE_HEARING);
        var caseData = details.getData();
        if (ManageHearingOptions.VACATE_HEARING.equals(caseData.getManageHearingOptions())) {
            log.info("caseData.getHearingListThatCanBeVacated() {}", caseData.getHearingListThatCanBeVacated());
            caseData.updateVacatedHearings();
        }
        if (ManageHearingOptions.ADJOURN_HEARING.equals(caseData.getManageHearingOptions())) {
            log.info("caseData.getHearingListThatCanBeAdjourned() {}", caseData.getHearingListThatCanBeAdjourned());
            caseData.updateAdjournHearings();
        }
        if (ManageHearingOptions.ADD_NEW_HEARING.equals(caseData.getManageHearingOptions())
            || YesOrNo.YES == caseData.getIsTheHearingNeedsRelisting()) {
            caseData.archiveHearingInformation();
        }

        caseData.setHearingA90Document(null);
        caseData.setHearingListThatCanBeVacated(null);
        caseData.setHearingListThatCanBeAdjourned(null);
        caseData.setReasonForAdjournHearing(null);
        caseData.setReasonForVacatingHearing(null);
        caseData.setIsTheHearingNeedsRelisting(null);

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
            caseData.getManageHearingDetails().setHearingCreationDate(LocalDate.now(clock));
            @SuppressWarnings("unchecked")
            Map<String, Object> templateContent = objectMapper.convertValue(caseData, Map.class);
            caseData.getManageHearingDetails().setHearingA90Document(caseDataDocumentService.renderDocument(
                templateContent,
                details.getId(),
                MANAGE_HEARING_NOTICES_A90,
                LanguagePreference.ENGLISH,
                MANAGE_HEARING_NOTICES_A90_FILE_NAME));
            caseData.setHearingA90Document(caseData.getManageHearingDetails().getHearingA90Document());
        }

        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .errors(error)
            .build();
    }
}
