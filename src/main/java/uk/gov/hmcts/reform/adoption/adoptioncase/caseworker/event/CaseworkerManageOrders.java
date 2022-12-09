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
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.AdoptionOrder;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.GeneralDirectionOrders;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.ManageOrders;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.AdoptionOrderData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.document.CaseDataDocumentService;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Optional;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.FINAL_ADOPTION_ORDER_A76;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.FINAL_ADOPTION_ORDER_A76_FILE_NAME;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.FINAL_ADOPTION_ORDER_A206_DRAFT;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.FINAL_ADOPTION_ORDER_A206_DRAFT_FILE_NAME;

/**
 * Contains method to define Event Configuration for ExUI.
 * Enable Manage orders functionality for Adoption Cases.
 */
@Component
@Slf4j
public class CaseworkerManageOrders implements CCDConfig<CaseData, State, UserRole> {

    @Autowired
    private CaseDataDocumentService caseDataDocumentService;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * The constant CASEWORKER_MANAGE_ORDERS.
     */
    public static final String CASEWORKER_MANAGE_ORDERS = "caseworker-manage-orders";
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
     * The constant MANAGE_ORDERS.
     */
    public static final String MANAGE_ORDERS = "Manage orders";

    private final CcdPageConfiguration manageOrders = new ManageOrders();
    private final CcdPageConfiguration adoptionOrder = new AdoptionOrder();
    private final CcdPageConfiguration generalDirectionOrder = new GeneralDirectionOrders();



    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        log.info("Inside configure method for Event {}", CASEWORKER_MANAGE_ORDERS);
        var pageBuilder = addEventConfig(configBuilder);
        manageOrders.addTo(pageBuilder);
        adoptionOrder.addTo(pageBuilder);
        generalDirectionOrder.addTo(pageBuilder);
        getRecipientsPage(pageBuilder);
        getPreviewPage(pageBuilder);
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
                                   .event(CASEWORKER_MANAGE_ORDERS)
                                   .forAllStates()
                                   .name(MANAGE_ORDERS)
                                   .description(MANAGE_ORDERS)
                                   .showSummary()
                                   .aboutToSubmitCallback(this::aboutToSubmit)
                                   .grant(Permissions.CREATE_READ_UPDATE, UserRole.CASE_WORKER)
                                   .grant(Permissions.CREATE_READ_UPDATE, UserRole.DISTRICT_JUDGE));
    }

    /**
     * Event method to reset the values for allowing any no.of new orders.
     * Gatekeeping Order, Directions Order & Final Adoption Order
     *
     * @param beforeDetails - Application CaseDetails for the previous page
     * @param details - Application CaseDetails for the present page
     * @return - AboutToStartOrSubmitResponse updated to use on further pages.
     */
    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(
        CaseDetails<CaseData, State> details,
        CaseDetails<CaseData, State> beforeDetails) {
        log.info("Callback invoked for {}", CASEWORKER_MANAGE_ORDERS);
        CaseData caseData = details.getData();
        caseData.archiveManageOrders();
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .errors(new ArrayList<>())
            .build();
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
                + "You can make changes on the next page.", "manageOrderType=\"finalAdoptionOrder\"", true)
            .complex(CaseData::getAdoptionOrderData)
            .readonly(AdoptionOrderData::getDraftDocumentA76)
            .readonly(AdoptionOrderData::getDraftDocumentA206)
            .done()
            .done();
    }

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
                objectMapper.convertValue(caseData, Map.class);

            if (isNotEmpty(selectedRecipientsA76)) {
                caseData.getAdoptionOrderData().setDraftDocumentA76(
                    caseDataDocumentService.renderDocument(
                        templateContent,
                        details.getId(),
                        FINAL_ADOPTION_ORDER_A76,
                        LanguagePreference.ENGLISH,
                        FINAL_ADOPTION_ORDER_A76_FILE_NAME
                    ));
            }

            if (isNotEmpty(selectedRecipientsA206)) {
                caseData.getAdoptionOrderData().setDraftDocumentA206(
                    caseDataDocumentService.renderDocument(
                        templateContent,
                        details.getId(),
                        FINAL_ADOPTION_ORDER_A206_DRAFT,
                        LanguagePreference.ENGLISH,
                        FINAL_ADOPTION_ORDER_A206_DRAFT_FILE_NAME
                    ));
            }
        }
        log.info("caseData.getAdoptionOrderData().setDraftDocumentA76 {}", caseData.getAdoptionOrderData().getDraftDocumentA76());
        log.info("caseData.getAdoptionOrderData().setDraftDocumentA206 {}", caseData.getAdoptionOrderData().getDraftDocumentA206());

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

}
