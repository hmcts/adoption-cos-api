package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.AdoptionOrder;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.ManageOrders;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

import java.util.ArrayList;

/**
 * Contains method to define Event Configuration for ExUI.
 * Enable Manage orders functionality for Adoption Cases.
 */
@Component
@Slf4j
public class CaseworkerManageOrders implements CCDConfig<CaseData, State, UserRole> {

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


    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        log.info("Inside configure method for Event {}", CASEWORKER_MANAGE_ORDERS);
        var pageBuilder = addEventConfig(configBuilder);
        manageOrders.addTo(pageBuilder);
        adoptionOrder.addTo(pageBuilder);
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
}
