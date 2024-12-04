package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.AdoptionOrder;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.GeneralDirectionOrders;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.ManageOrders;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.AdoptionOrderData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ManageOrdersData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.DirectionsOrderData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.document.CaseDataDocumentService;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Set;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static uk.gov.hmcts.reform.adoption.adoptioncase.validation.RecipientValidationUtil.validateRecipients;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.FINAL_ADOPTION_ORDER_A76_DRAFT;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.FINAL_ADOPTION_ORDER_A76_DRAFT_FILE_NAME;
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
        return new PageBuilder(configBuilder
                                   .event(CASEWORKER_MANAGE_ORDERS)
                                   .forAllStates()
                                   .name(MANAGE_ORDERS)
                                   .description(MANAGE_ORDERS)
                                   .showSummary()
                                   .aboutToSubmitCallback(this::aboutToSubmit)
        );
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
        pageBuilder.page("manageOrders6", this::midEventManageOrderRecipients)
            .showCondition("manageOrderType=\"caseManagementOrder\"")
            .pageLabel("Case management order recipients")
            .complex(CaseData::getManageOrdersData)
            .label("LabelRecipients661", "#### Select who to serve the order to", null, true)
            .mandatory(ManageOrdersData::getRecipientsList)
            .done()
            .done();

        pageBuilder.page("manageOrders9", this::midEventFinalRecipients)
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

        pageBuilder.page("manageOrderGdRecipients",this::midEventGeneralDirectionRecipients)
            .showCondition(
                "manageOrderType=\"generalDirectionsOrder\" AND generalDirectionOrderTypes=\"generalDirectionProductionOrder\"")
            .label("generalDirectionRecipientsListLabel", "## Recipients of this order")
            .label("generalDirectionWhomToServe", "### Select who to serve the order to")
            .complex(CaseData::getDirectionsOrderData)
            .label("directionOrderRecipientsData","You can select more than one person. It is important that recipients are <br>"
                + "checked carefully to make sure this order is not served to the wrong person")
            .mandatory(DirectionsOrderData::getGeneralDirectionRecipientsList)
            .done();
    }

    public AboutToStartOrSubmitResponse<CaseData, State> midEventManageOrderRecipients(CaseDetails<CaseData, State> details,
        CaseDetails<CaseData, State> detailsBefore) {
        CaseData caseData = details.getData();

        return validateRecipients(caseData.getManageOrdersData().getRecipientsList(), null, caseData, new ArrayList<>());
    }

    public AboutToStartOrSubmitResponse<CaseData, State> midEventGeneralDirectionRecipients(CaseDetails<CaseData, State> details,
        CaseDetails<CaseData, State> detailsBefore) {
        CaseData caseData = details.getData();

        return validateRecipients(caseData.getDirectionsOrderData()
                            .getGeneralDirectionRecipientsList(), null, caseData, new ArrayList<>());
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

        pageBuilder.page("manageOrderGdPreview")
            .showCondition(
                "manageOrderType=\"generalDirectionsOrder\" AND generalDirectionOrderTypes=\"generalDirectionProductionOrder\"")
            .pageLabel("Preview the draft order")
            .label("generalDirectionPreviewOrderLab","## Preview the order")
            .label("generalDirectionPreviewDocsLab2","### Document to review")
            .label("generalDirectionPreviewDocsLab3","This document will open a new page when you select it")
            .complex(CaseData::getDirectionsOrderData)
            .readonly(DirectionsOrderData::getGeneralDirectionDraftDocument)
            .done()
            .label("generalDirectionChangeAlert","If you want to make further changes, go back to the previous screen")
            .done();
    }

    public AboutToStartOrSubmitResponse<CaseData, State> midEventFinalRecipients(
        CaseDetails<CaseData, State> details,
        CaseDetails<CaseData, State> detailsBefore
    ) {
        CaseData caseData = details.getData();
        caseData.setAllocatedJudge(detailsBefore.getData().getAllocatedJudge());
        Set<AdoptionOrderData.RecipientsA76> selectedRecipientsA76 = caseData.getAdoptionOrderData().getRecipientsListA76();
        Set<AdoptionOrderData.RecipientsA206> selectedRecipientsA206 = caseData.getAdoptionOrderData().getRecipientsListA206();
        List<String> errors = new ArrayList<>();

        AboutToStartOrSubmitResponse<CaseData, State> aboutToStartOrSubmitResponse =
            validateRecipients(selectedRecipientsA76, selectedRecipientsA206, caseData, errors);
        if (isEmpty(aboutToStartOrSubmitResponse.getErrors())) {
            @SuppressWarnings("unchecked")
            Map<String, Object> templateContent =
                objectMapper.convertValue(caseData, Map.class);

            if (isNotEmpty(selectedRecipientsA76)) {
                caseData.getAdoptionOrderData().setDraftDocumentA76(
                    caseDataDocumentService.renderDocument(
                        templateContent,
                        details.getId(),
                        FINAL_ADOPTION_ORDER_A76_DRAFT,
                        LanguagePreference.ENGLISH,
                        FINAL_ADOPTION_ORDER_A76_DRAFT_FILE_NAME
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
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .errors(errors)
            .build();
    }
}
