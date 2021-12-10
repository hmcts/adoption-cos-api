package uk.gov.hmcts.reform.adoption.adoptioncase.tab;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.CASE_WORKER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.LEGAL_ADVISOR;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SOLICITOR;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SUPER_USER;

@Component
public class CaseTypeTab implements CCDConfig<CaseData, State, UserRole> {

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        buildAosTab(configBuilder);
        buildPaymentTab(configBuilder);
        buildLanguageTab(configBuilder);
        buildDocumentsTab(configBuilder);
        buildConfidentialApplicantTab(configBuilder);
        buildNotesTab(configBuilder);
        buildConfidentialDocumentsTab(configBuilder);
        buildServiceApplicationTab(configBuilder);
    }

    //TODO: Need to revisit this tab once the field stated
    private void buildAosTab(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.tab("aosDetails", "AoS")
            .forRoles(CASE_WORKER, LEGAL_ADVISOR,
                      SUPER_USER, SOLICITOR)
            .label("LabelAosTabOnlineResponse-Heading", null, "## This is an online AoS response")
            .field("confirmReadPetition")
            .field("jurisdictionAgree")
            .field("jurisdictionDisagreeReason")
            .field("digitalNoticeOfProceedings")
            .field("noticeOfProceedingsEmail");
    }

    private void buildPaymentTab(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.tab("paymentDetailsCourtAdmin", "Payment")
            .field("applicant1HWFReferenceNumber");
    }

    private void buildLanguageTab(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.tab("languageDetails", "Language")
            .label("LabelLanguageDetails-Applicant", null, "### The applicant")
            .field("applicant1LanguagePreferenceWelsh");
    }

    private void buildDocumentsTab(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.tab("documents", "Documents")
            .field("certificateOfServiceDocument")
            .field("coCertificateOfEntitlementDocument");
    }

    private void buildConfidentialApplicantTab(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.tab("ConfidentialApplicant", "Confidential Address")
            .forRoles(CASE_WORKER, LEGAL_ADVISOR)
            .showCondition("applicant1KeepContactDetailsConfidential=\"Yes\"")
            .field("applicant1CorrespondenceAddress")
            .field("applicant1PhoneNumber")
            .field("applicant1Email")
            .field("applicant1HomeAddress");
    }

    private void buildNotesTab(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.tab("notes", "Notes")
            .field(CaseData::getDueDate);
    }

    private void buildConfidentialDocumentsTab(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.tab("confidentialDocuments", "Confidential Document")
            .forRoles(CASE_WORKER, LEGAL_ADVISOR, SUPER_USER)
            .field(CaseData::getApplication);
    }

    private void buildServiceApplicationTab(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.tab("alternativeService", "Service Application")
            .forRoles(CASE_WORKER, LEGAL_ADVISOR, SUPER_USER)
            .field("receivedServiceApplicationDate")
            .field("receivedServiceAddedDate")
            .field("alternativeServiceType")
            .field("paymentMethod")
            .field("dateOfPayment", "paymentMethod=\"*\"")
            .field("feeAccountNumber", "paymentMethod=\"feePayByAccount\"")
            .field("feeAccountReferenceNumber", "paymentMethod=\"feePayByAccount\"")
            .field("helpWithFeesReferenceNumber", "paymentMethod=\"feePayByHelp\"");
    }
}
