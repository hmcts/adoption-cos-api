package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Children;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.AdoptionOrderData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.document.CaseDataDocumentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.FINAL_ADOPTION_ORDER_A76;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.FINAL_ADOPTION_ORDER_A76_FILE_NAME;
import static uk.gov.hmcts.reform.adoption.document.DocumentUtil.formatDocumentName;

/**
 * Contains method to add Page Configuration for ExUI.
 * Display the Manage orders Details screen with all required fields.
 */
@Slf4j
public class AdoptionOrder implements CcdPageConfiguration, ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static final String ERROR_CHECK_RECIPIENTS_SELECTION = "Recipients of Final adoption order is required";
    public static final String FIRST_APPLICANT_NOT_APPLICABLE = "First Applicant not applicable for the case";
    public static final String SECOND_APPLICANT_NOT_APPLICABLE = "Second Applicant not applicable for the case";
    public static final String BIRTH_MOTHER_NOT_APPLICABLE = "Recipient birth mother not applicable for the case";
    public static final String BIRTH_FATHER_NOT_APPLICABLE = "Recipient birth father not applicable for the case";
    public static final String LEGAL_GUARDIAN_NOT_APPLICABLE = "Legal guardian (CAFCASS) not applicable for the case";
    public static final String CHILDS_LA_NOT_APPLICABLE = "Child local authority not applicable for the case";
    public static final String APPLICANTS_LA_NOT_APPLICABLE = "Applicants local authority not applicable for the case";
    public static final String ADOP_AGENCY_NOT_APPLICABLE = "Adoption agency not applicable for the case";
    public static final String OTHER_ADOP_AGENCY_NOT_APPLICABLE = "Other adoption agency not applicable for the case";
    public static final String OTHER_PARENT_AGENCY_NOT_APPLICABLE = "Other person with parental responsibility not applicable for the case";

    /**
     * Overridden method to define page design and flow for entire event / Journey.
     *
     * @param pageBuilder - Application PageBuilder for the event pages
     */
    @Override
    public void addTo(PageBuilder pageBuilder) {
        getFinalOrderPage(pageBuilder);
        getRecipientsPage(pageBuilder);
        getPreviewPage(pageBuilder);
    }

    /**
     * Helper method to support page design and flow to display complete list of Optional Fields for further selection.
     * Final Adoption Order
     *
     * @param pageBuilder - Application PageBuilder for the event pages
     */
    private void getFinalOrderPage(PageBuilder pageBuilder) {
        pageBuilder.page("manageOrders7")
            .showCondition("manageOrderType=\"finalAdoptionOrder\"")
            .label("pageLabel71","Please select all the relevant options for this order."
                +  " The directions attached to each option can be reviewed on the next screens. "
                +  "You can change your options by returning to the previous screens.")
            .complex(CaseData::getAdoptionOrderData)
            .label("LabelPreamble71", "### Preamble", null, true)
            .optional(AdoptionOrderData::getPreambleDetailsFinalAdoptionOrder)
            .label("LabelOrderedBy72","### Ordered by", null, true)
            .mandatory(AdoptionOrderData::getOrderedByFinalAdoptionOrder)
            .label("LabelDateOrderMade76","### Date order made", null, true)
            .mandatory(AdoptionOrderData::getDateOrderMadeFinalAdoptionOrder)
            .label("LabelOrderedBy73","### Placement of the child", null, true)
            .mandatory(AdoptionOrderData::getPlacementOfTheChildList)
            .done()
            .complex(CaseData::getChildren)
            .label("LabelChildFullNameAfterAdoption74","### Child's full name after adoption", null, true)
            .mandatory(Children::getFirstNameAfterAdoption)
            .mandatory(Children::getLastNameAfterAdoption)
            .done()
            .complex(CaseData::getAdoptionOrderData)
            .label("LabelCostOrders75","### Cost orders", null, true)
            .optional(AdoptionOrderData::getCostOrdersFinalAdoptionOrder)
            .done();

        pageBuilder.page("manageOrders8")
            .showCondition("manageOrderType=\"finalAdoptionOrder\"")
            .complex(CaseData::getAdoptionOrderData)
            .label("pageLabel81","## Final adoption order date and place of birth")
            .label("pageLabel82","### Has place of birth been proved?", null, true)
            .mandatory(AdoptionOrderData::getPlaceOfBirthProved)
            .label("pageLabel83","#### Choose the type of certificate","placeOfBirthProved=\"Yes\"", true)
            .mandatory(AdoptionOrderData::getTypeOfCertificate,"placeOfBirthProved=\"Yes\"")
            .label("pageLabel84","#### Choose the country of birth","placeOfBirthProved=\"Yes\"", true)
            .mandatory(AdoptionOrderData::getCountryOfBirthForPlaceOfBirthYes,"placeOfBirthProved=\"Yes\"")
            .mandatory(AdoptionOrderData::getOtherCountryOfOriginForPlaceOfBirthYes,
                       "countryOfBirthForPlaceOfBirthYes=\"outsideTheUK\" AND placeOfBirthProved=\"Yes\"")
            .label("pageLabel85","#### Choose a probable birth location","placeOfBirthProved=\"No\"", true)
            .mandatory(AdoptionOrderData::getCountryOfBirthForPlaceOfBirthNo,"placeOfBirthProved=\"No\"")
            .mandatory(AdoptionOrderData::getOtherCountryOfOriginForPlaceOfBirthNo,
                       "countryOfBirthForPlaceOfBirthNo=\"outsideTheUK\" AND placeOfBirthProved=\"No\"")
            .label("pageLabel86","### Is time of birth known?", null, true)
            .mandatory(AdoptionOrderData::getTimeOfBirthKnown)
            .label("pageLabel87","#### Time of birth","timeOfBirthKnown=\"Yes\"", true)
            .mandatory(AdoptionOrderData::getTimeOfBirth,"timeOfBirthKnown=\"Yes\"")
            .label("pageLabel88","### Birth adoption registration number", null,  true)
            .mandatory(AdoptionOrderData::getBirthAdoptionRegistrationNumber)
            .label("pageLabel89","### Birth/Adoption registration date", null, true)
            .mandatory(AdoptionOrderData::getAdoptionRegistrationDate)
            .label("pageLabel810","### Registration district", null, true)
            .mandatory(AdoptionOrderData::getRegistrationDistrict)
            .label("pageLabel811","### Registration sub-district",null, true)
            .mandatory(AdoptionOrderData::getRegistrationSubDistrict)
            .label("pageLabel812","### Registration county",null, true)
            .mandatory(AdoptionOrderData::getRegistrationCounty)
            .done()
            .done();
    }

    /**
     * Helper method to support page design and flow to display Final Order Recipients List in 2 categories.
     * Final Adoption Order Recipients
     *
     * @param pageBuilder - Application PageBuilder for the event pages
     */
    private void getRecipientsPage(PageBuilder pageBuilder) {
        pageBuilder.page("manageOrders9", this::midEventRecipients)
            .showCondition("manageOrderType=\"finalAdoptionOrder\"")
            .pageLabel("Final adoption order recipients")
            .complex(CaseData::getAdoptionOrderData)
            .label("LabelRecipients91", "#### Select who to serve the order to", null, true)
            .label("LabelRecipients92", "###### <p>Only select people who are party to this case and who "
                + "need a copy of this order. The General Register Office will be automatically sent a copy.</p>")
            .optional(AdoptionOrderData::getRecipientsListA76)
            .optional(AdoptionOrderData::getRecipientsListA206)
            .done()
            .done();
    }

    /**
     * Helper method to support page design and flow to display Final Order Preview Draft.
     * Final Adoption Order Preview
     *
     * @param pageBuilder - Application PageBuilder for the event pages
     */
    private void getPreviewPage(PageBuilder pageBuilder) {
        pageBuilder.page("manageOrders10")
            .showCondition("manageOrderType=\"finalAdoptionOrder\"")
            .pageLabel("Preview the draft order")
            .label("LabelPreview101","Preview and check the order in draft. "
                + "You can make changes on the next page.")
            .complex(CaseData::getAdoptionOrderData)
            .optional(AdoptionOrderData::getDraftDocument)
            .done()
            .done();
    }

    /**
     * Event method to validate the right selection options done by the User as per the Requirements for Recipients.
     * Final Adoption Order Recipients
     *
     * @param detailsBefore - Application CaseDetails for the previous page
     * @param details - Application CaseDetails for the present page
     * @return - AboutToStartOrSubmitResponse updated to use on further pages.
     */
    public AboutToStartOrSubmitResponse<CaseData, State> midEventRecipients(
        CaseDetails<CaseData, State> details,
        CaseDetails<CaseData, State> detailsBefore
    ) {
        CaseData caseData = details.getData();
        final List<String> errors = new ArrayList<>();
        caseData.setAllocatedJudge(detailsBefore.getData().getAllocatedJudge());
        Set<AdoptionOrderData.RecipientsA76> selectedRecipientsA76 = caseData.getAdoptionOrderData().getRecipientsListA76();
        Set<AdoptionOrderData.RecipientsA206> selectedRecipientsA206 = caseData.getAdoptionOrderData().getRecipientsListA206();

        if (isEmpty(selectedRecipientsA76) && isEmpty(selectedRecipientsA206)) {
            errors.add(ERROR_CHECK_RECIPIENTS_SELECTION);
            return AboutToStartOrSubmitResponse.<CaseData, State>builder()
                .data(caseData)
                .errors(errors)
                .build();
        }
        if (isNotEmpty(selectedRecipientsA76)) {
            if (selectedRecipientsA76.contains(AdoptionOrderData.RecipientsA76.FIRST_APPLICANT)
                && isNotEmpty(caseData.getApplicant1()) && isEmpty(caseData.getApplicant1().getFirstName())) {
                errors.add(FIRST_APPLICANT_NOT_APPLICABLE);
            }
            if (selectedRecipientsA76.contains(AdoptionOrderData.RecipientsA76.SECOND_APPLICANT)
                && isNotEmpty(caseData.getApplicant2()) && isEmpty(caseData.getApplicant2().getFirstName())) {
                errors.add(SECOND_APPLICANT_NOT_APPLICABLE);
            }
        }
        if (isNotEmpty(selectedRecipientsA206)) {
            selectedRecipientsA206.forEach(recipient -> Optional.ofNullable(isApplicableA206(recipient, caseData)).ifPresent(errors::add));
        }
        if (isEmpty(errors)) {
            @SuppressWarnings("unchecked")
            Map<String, Object> templateContent =
                applicationContext.getBean(ObjectMapper.class).convertValue(caseData, Map.class);
            log.info("templateContent {}", templateContent);
            caseData.getAdoptionOrderData().setDraftDocument(
                applicationContext.getBean(CaseDataDocumentService.class).renderDocument(
                templateContent,
                details.getId(),
                FINAL_ADOPTION_ORDER_A76,
                LanguagePreference.ENGLISH,
                formatDocumentName(
                    details.getId(),
                    FINAL_ADOPTION_ORDER_A76_FILE_NAME,
                    LocalDateTime.now()
                )));
        }
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .errors(errors)
            .build();
    }

    private String isApplicableA206(AdoptionOrderData.RecipientsA206 choice, CaseData caseData) {
        switch (choice) {
            case RESPONDENT_BIRTH_MOTHER:
                if (isEmpty(caseData.getBirthMother().getFirstName())) {
                    return BIRTH_MOTHER_NOT_APPLICABLE;
                }
                break;
            case RESPONDENT_BIRTH_FATHER:
                if (isEmpty(caseData.getBirthFather().getFirstName())) {
                    return BIRTH_FATHER_NOT_APPLICABLE;
                }
                break;
            case LEGAL_GUARDIAN_CAFCASS:
                if (isEmpty(caseData.getIsChildRepresentedByGuardian())
                    || caseData.getIsChildRepresentedByGuardian().equals(YesOrNo.NO)) {
                    return LEGAL_GUARDIAN_NOT_APPLICABLE;
                }
                break;
            case CHILDS_LOCAL_AUTHORITY:
                if (isEmpty(caseData.getChildSocialWorker().getLocalAuthority())) {
                    return CHILDS_LA_NOT_APPLICABLE;
                }
                break;
            case APPLICANTS_LOCAL_AUTHORITY:
                if (isEmpty(caseData.getApplicantSocialWorker().getLocalAuthority())) {
                    return APPLICANTS_LA_NOT_APPLICABLE;
                }
                break;
            case ADOPTION_AGENCY:
                if (isEmpty(caseData.getAdopAgencyOrLA().getAdopAgencyOrLaName())) {
                    return ADOP_AGENCY_NOT_APPLICABLE;
                }
                break;
            case OTHER_ADOPTION_AGENCY:
                if (isEmpty(caseData.getHasAnotherAdopAgencyOrLAinXui())
                    || caseData.getHasAnotherAdopAgencyOrLAinXui().equals(YesOrNo.NO)) {
                    return OTHER_ADOP_AGENCY_NOT_APPLICABLE;
                }
                break;
            default:
                if (isEmpty(caseData.getIsThereAnyOtherPersonWithParentalResponsibility())
                    || caseData.getIsThereAnyOtherPersonWithParentalResponsibility().equals(YesOrNo.NO)) {
                    return OTHER_PARENT_AGENCY_NOT_APPLICABLE;
                }
                break;
        }
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) {
        applicationContext = appContext;
    }
}
