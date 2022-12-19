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
import uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Parent;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageHearingOptions;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageHearingDetails;
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
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.MANAGE_HEARING_NOTICES_A90;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.MANAGE_HEARING_NOTICES_A91;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.MANAGE_HEARING_NOTICES_A90_FILE_NAME;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.MANAGE_HEARING_NOTICES_A91_FILE_NAME_MOTHER;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.MANAGE_HEARING_NOTICES_A91_FILE_NAME_FATHER;

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
            .complex(CaseData::getManageHearingDetails)
            .mandatory(ManageHearingDetails::getRecipientsInTheCase)
            .done()
            .done();

        pageBuilder.page("manageHearing7")
            .showCondition("manageHearingOptions=\"addNewHearing\" OR isTheHearingNeedsRelisting=\"Yes\"")
            .pageLabel("Preview the hearing notice")
            .complex(CaseData::getBirthMother, "recipientsInTheCaseCONTAINS\"NEVER DISPLAY CONDITION\"")
            .readonly(Parent::getDeceased, "recipientsInTheCaseCONTAINS\"NEVER DISPLAY CONDITION\"")
            .done()
            .complex(CaseData::getBirthFather, "recipientsInTheCaseCONTAINS\"NEVER DISPLAY CONDITION\"")
            .readonly(Parent::getDeceased, "recipientsInTheCaseCONTAINS\"NEVER DISPLAY CONDITION\"")
            .done()
            .label("manageHearing71","## Preview the hearing notice",null, true)
            .label("manageHearing72","### Document to review",null, true)
            .label("manageHearing73","This document will open in a new page when you select it")
            .complex(CaseData::getManageHearingDetails)
            .label("manageHearing74","### Respondent (birth mother)",
                   "recipientsInTheCaseCONTAINS\"respondentBirthMother\" AND birthMotherDeceased=\"No\"")
            .readonly(ManageHearingDetails::getHearingA91DocumentMother,
                      "recipientsInTheCaseCONTAINS\"respondentBirthMother\" AND birthMotherDeceased=\"No\"")
            .label("manageHearing75","### Respondent (birth father)",
                   "recipientsInTheCaseCONTAINS\"respondentBirthFather\" AND birthFatherDeceased=\"No\"")
            .readonly(ManageHearingDetails::getHearingA91DocumentFather,
                      "recipientsInTheCaseCONTAINS\"respondentBirthFather\" AND birthFatherDeceased=\"No\"")
            .label("manageHearing76","### Applicants",
                   "recipientsInTheCaseCONTAINS\"applicant1\" OR recipientsInTheCaseCONTAINS\"applicant2\"")
            .readonly(ManageHearingDetails::getHearingA90Document,
                      "recipientsInTheCaseCONTAINS\"applicant1\" OR recipientsInTheCaseCONTAINS\"applicant2\"")
            .label("manageHearing77","You can make changes to the notice by continuing to the next page")
            .done()
            .done();
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
        if (ManageHearingOptions.VACATE_HEARING.equals(caseData.getManageHearingDetails().getManageHearingOptions())) {
            caseData.updateVacatedHearings();
        }
        if (ManageHearingOptions.ADJOURN_HEARING.equals(caseData.getManageHearingDetails().getManageHearingOptions())) {
            caseData.updateAdjournHearings();
        }
        if (ManageHearingOptions.ADD_NEW_HEARING.equals(caseData.getManageHearingDetails().getManageHearingOptions())
            || YesOrNo.YES == caseData.getManageHearingDetails().getIsTheHearingNeedsRelisting()) {
            caseData.archiveHearingInformation();
        }
        caseData.setHearingListThatCanBeVacated(null);
        caseData.setHearingListThatCanBeAdjourned(null);

        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .build();
    }

    /**
     * This MidEvent will validate if any incorrect selection of Recipients is made.
     * In case any non-applicable Recipient is selected, System will throw an error.
     *
     * @param detailsBefore - Application CaseDetails for the previous page
     * @param details - Application CaseDetails for the present page
     * @return - AboutToStartOrSubmitResponse updated to use on further pages.
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
            caseData.setBirthMother(detailsBefore.getData().getBirthMother());
            caseData.setBirthFather(detailsBefore.getData().getBirthFather());
            caseData.getManageHearingDetails().setHearingA90Document(null);
            caseData.getManageHearingDetails().setHearingA91DocumentMother(null);
            caseData.getManageHearingDetails().setHearingA91DocumentFather(null);

            caseData.getManageHearingDetails().setHearingCreationDate(LocalDate.now(clock));
            caseData.getManageHearingDetails().getRecipientsInTheCase().forEach(recipientsInTheCase -> {
                switch (recipientsInTheCase) {
                    case APPLICANT1: case APPLICANT2:
                        if (isEmpty(caseData.getManageHearingDetails().getHearingA90Document())) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> templateContentApplicants = objectMapper.convertValue(caseData, Map.class);

                            caseData.getManageHearingDetails().setHearingA90Document(
                                caseDataDocumentService.renderDocument(
                                    templateContentApplicants,
                                    details.getId(),
                                    MANAGE_HEARING_NOTICES_A90,
                                    LanguagePreference.ENGLISH,
                                    MANAGE_HEARING_NOTICES_A90_FILE_NAME));
                        }
                        break;
                    case RESPONDENT_MOTHER:
                        if (isNotEmpty(caseData.getBirthMother().getDeceased())
                            && caseData.getBirthMother().getDeceased().equals(YesOrNo.NO)) {
                            caseData.getManageHearingDetails().setHearingA91DocumentFlagFather(YesOrNo.NO);
                            caseData.getManageHearingDetails().setHearingA91DocumentFlagMother(YesOrNo.YES);
                            @SuppressWarnings("unchecked")
                            Map<String, Object> templateContentMother = objectMapper.convertValue(caseData, Map.class);

                            caseData.getManageHearingDetails().setHearingA91DocumentMother(
                                caseDataDocumentService.renderDocument(
                                    templateContentMother,
                                    details.getId(),
                                    MANAGE_HEARING_NOTICES_A91,
                                    LanguagePreference.ENGLISH,
                                    MANAGE_HEARING_NOTICES_A91_FILE_NAME_MOTHER));
                        }
                        break;
                    case RESPONDENT_FATHER:
                        if (isNotEmpty(caseData.getBirthFather().getDeceased())
                            && caseData.getBirthFather().getDeceased().equals(YesOrNo.NO)) {
                            caseData.getManageHearingDetails().setHearingA91DocumentFlagFather(YesOrNo.YES);
                            caseData.getManageHearingDetails().setHearingA91DocumentFlagMother(YesOrNo.NO);
                            @SuppressWarnings("unchecked")
                            Map<String, Object> templateContentMother = objectMapper.convertValue(caseData, Map.class);

                            caseData.getManageHearingDetails().setHearingA91DocumentFather(
                                caseDataDocumentService.renderDocument(
                                    templateContentMother,
                                    details.getId(),
                                    MANAGE_HEARING_NOTICES_A91,
                                    LanguagePreference.ENGLISH,
                                    MANAGE_HEARING_NOTICES_A91_FILE_NAME_FATHER));
                        }
                        break;
                    default:
                        break;
                }
            });
        }

        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .errors(error)
            .build();
    }
}
