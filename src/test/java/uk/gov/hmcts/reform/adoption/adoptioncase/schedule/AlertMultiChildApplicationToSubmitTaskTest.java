package uk.gov.hmcts.reform.adoption.adoptioncase.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.service.CcdSearchService;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.adoption.notification.MultiChildSubmitAlertEmailNotification;
import uk.gov.hmcts.reform.adoption.systemupdate.CaseDetailsConverter;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.idam.client.models.User;
import uk.gov.hmcts.reform.idam.client.models.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.existsQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Draft;
import static uk.gov.hmcts.reform.adoption.adoptioncase.service.CcdSearchService.CREATED_DATE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.service.CcdSearchService.STATE;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(SpringExtension.class)
class AlertMultiChildApplicationToSubmitTaskTest {

    @InjectMocks
    private AlertMultiChildApplicationToSubmitTask alertMultiChildApplicationToSubmitTask;

    @Mock
    private CcdSearchService ccdSearchService;

    @Mock
    private IdamService idamService;

    @Mock
    private AuthTokenGenerator authTokenGenerator;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private MultiChildSubmitAlertEmailNotification multiChildSubmitAlertEmailNotification;

    @Mock
    private CaseDetailsConverter caseDetailsConverter;

    public static final String SYSTEM_UPDATE_AUTH_TOKEN = "Bearer SystemUpdateAuthToken";

    public static final String SERVICE_AUTHORIZATION = "ServiceAuthorization";


    private User user;

    private static final BoolQueryBuilder query = boolQuery()
        .must(matchQuery(STATE, Draft))
        .must(existsQuery(CREATED_DATE))
        .filter(rangeQuery(CREATED_DATE)
                    .gte(LocalDate.now().minusDays(83))
                    .lte(LocalDate.now().minusDays(83)));


    @BeforeEach
    void setUp() {
        user = new User(SYSTEM_UPDATE_AUTH_TOKEN, UserDetails.builder().build());
        when(idamService.retrieveSystemUpdateUserDetails()).thenReturn(user);
        when(authTokenGenerator.generate()).thenReturn(SERVICE_AUTHORIZATION);
    }

    @Test
    void run() {
        final var data = caseData();
        final CaseDetails caseDetails1 = mock(CaseDetails.class);
        final CaseDetails caseDetails2 = mock(CaseDetails.class);
        final CaseDetails caseDetails3 = mock(CaseDetails.class);

        when(caseDetails1.getCreatedDate()).thenReturn(LocalDateTime.now());
        when(caseDetails2.getCreatedDate()).thenReturn(LocalDateTime.now());
        when(caseDetails3.getCreatedDate()).thenReturn(LocalDateTime.now());
        when(caseDetails1.getState()).thenReturn(String.valueOf(State.Submitted));
        when(caseDetails2.getState()).thenReturn(String.valueOf(Draft));
        when(caseDetails3.getState()).thenReturn(String.valueOf(State.Submitted));
        final List<CaseDetails> caseDetailsList = List.of(caseDetails1, caseDetails2, caseDetails3);
        when(ccdSearchService.searchForAllCasesWithQuery(Draft, query, user, SERVICE_AUTHORIZATION))
            .thenReturn(caseDetailsList);
        final uk.gov.hmcts.ccd.sdk.api.CaseDetails<CaseData, State> caseDetails4 = new uk.gov.hmcts.ccd.sdk.api.CaseDetails<>();
        caseDetails4.setData(caseData());
        when(caseDetailsConverter.convertToCaseDetailsFromReformModel(any(CaseDetails.class))).thenReturn(caseDetails4);

        alertMultiChildApplicationToSubmitTask.run();
        verify(multiChildSubmitAlertEmailNotification, times(0)).sendToApplicants(
                                                      any(CaseData.class),
                                                      any(Long.class));

    }
}
