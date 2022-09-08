package uk.gov.hmcts.reform.adoption.adoptioncase.tab;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.CASE_WORKER;

@Component
public class CaseTypeTab implements CCDConfig<CaseData, State, UserRole> {

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        buildSummaryTab(configBuilder);
        buildConfidentialTab(configBuilder);
        buildDocumentsTab(configBuilder);
    }

    public void buildSummaryTab(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.tab("summary", "Summary")
            .forRoles(CASE_WORKER)
            .label("labelSummary-CaseStatus", null, "### Case status")
            .field("status")
            .field("messages")
            .label("labelSummary-CaseDetails", null, "### Case details")
            .field("typeOfAdoption")
            .field("dateSubmitted")
            .field("timetable20Week")
            .field("dateChildMovedIn")
            .field("applicationPayments")
            .field("familyCourtName")
            .field("placementOrderCourt")
            .field("placementOrders")
            .field("siblings");
    }

    private void buildConfidentialTab(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.tab("Confidential", "Confidential Details")
            .forRoles(CASE_WORKER)
            .field("applicant1PhoneNumber")
            .field("applicant1EmailAddress")
            .field("childrenFirstName")
            .field("childrenLastName")
            .field("applyingWith");
    }

    private void buildDocumentsTab(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.tab("documents", "Documents")
            .forRoles(CASE_WORKER)
            .field(CaseData::getDocumentsGenerated)
            .field(CaseData::getApplicant1DocumentsUploaded)
            .field(CaseData::getDocumentsUploaded);
    }
}
