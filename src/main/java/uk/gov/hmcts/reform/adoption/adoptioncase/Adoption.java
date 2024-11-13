package uk.gov.hmcts.reform.adoption.adoptioncase;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Draft;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.LaSubmitted;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Submitted;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.CASE_WORKER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.CITIZEN;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.COURT_ADMIN;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.DISTRICT_JUDGE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.LEGAL_ADVISOR;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SOLICITOR;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SUPER_USER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SYSTEM_UPDATE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions.CREATE_READ_UPDATE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions.READ;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AwaitingPayment;


@Component
public class Adoption implements CCDConfig<CaseData, State, UserRole> {
    public static final String CASE_TYPE = "A58";
    public static final String JURISDICTION = "ADOPTION";

    //@Autowired
    //private AddSystemUpdateRole addSystemUpdateRole;

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.setCallbackHost(System.getenv().getOrDefault("CASE_API_URL", "http://localhost:4550"));
        /*configBuilder.caseType(CASE_TYPE, "Post Placement", "Post Placement");
        configBuilder.caseType(CASE_TYPE, "Step Parent", "Step Parent");
        configBuilder.caseType(CASE_TYPE, "Relinquished", "Relinquished");
        configBuilder.caseType(CASE_TYPE, "International", "International");*/
        configBuilder.caseType(CASE_TYPE, "New Adoption case", "New Adoption case");
        configBuilder.jurisdiction(JURISDICTION, "Adoption", "Child adoption");

        configBuilder.grant(Draft, CREATE_READ_UPDATE, CITIZEN);
        /* configBuilder.grant(Draft, READ, SOLICITOR);
        configBuilder.grant(Draft, READ, SUPER_USER);
        configBuilder.grant(Draft, READ, CASE_WORKER);
        configBuilder.grant(Draft, READ, COURT_ADMIN); */
        //configBuilder.grant(Draft, READ, SUPER_USER);
        configBuilder.grant(AwaitingPayment, READ, SYSTEM_UPDATE);
        /* configBuilder.grant(Draft, READ, LEGAL_ADVISOR);
        configBuilder.grant(Draft, READ, DISTRICT_JUDGE); */
        configBuilder.grant(Draft, CREATE_READ_UPDATE, SYSTEM_UPDATE);
        configBuilder.grant(Submitted, CREATE_READ_UPDATE, SYSTEM_UPDATE);
        configBuilder.grant(LaSubmitted, CREATE_READ_UPDATE, SYSTEM_UPDATE);

        /* TODO
        Spreadsheet AuthorisationCaseState still has:
        [CREATOR] (CRU) - ok
        caseworker-adoption-caseworker = CASE_WORKER (CRU)
        caseworker-adoption-courtadmin = COURT_ADMIN (RU)
        caseworker-adoption-judge = DISTRICT_JUDGE (CRU)
        caseworker-adoption-la = LEGAL_ADVISOR (RU)
        caseworker-adoption-systemupdate (CRU) - ok?
        citizen (CRU) - ok

        This change successfully removed:
        caseworker-adoption-solicitor = SOLICITOR
        caseworker-adoption-superuser = SUPER_USER

        Might need to add SUPER_USER back for AwaitingPayment
         */
    }
}
