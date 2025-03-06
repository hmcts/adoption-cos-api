package uk.gov.hmcts.reform.adoption.adoptioncase.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.Event;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.common.service.SendNotificationService;
import uk.gov.hmcts.reform.adoption.common.service.SubmissionService;
import uk.gov.hmcts.reform.adoption.service.task.EventService;
import uk.gov.hmcts.reform.ccd.client.model.SubmittedCallbackResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.adoption.adoptioncase.event.LocalAuthoritySubmitApplication.LOCAL_AUTHORITY_SUBMIT;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.LaSubmitted;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Submitted;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(MockitoExtension.class)
public class LocalAuthoritySubmitApplicationTest extends EventTest {

    @InjectMocks
    private LocalAuthoritySubmitApplication localAuthoritySubmitApplication;

    @Mock
    private SendNotificationService sendNotificationService;

    @Mock
    private SubmissionService submissionService;

    @Mock
    private EventService eventPublisher;

    @Test
    void localAuthoritySubmitApplicationConfigure() {
        final ConfigBuilderImpl<CaseData, State, UserRole> configBuilder = createCaseDataConfigBuilder();
        localAuthoritySubmitApplication.configure(configBuilder);
        assertThat(getEventsFrom(configBuilder).values())
            .extracting(Event::getId)
            .contains(LOCAL_AUTHORITY_SUBMIT);
    }

    @Test
    void localAuthoritySubmitApplicationSubmitted() {
        var caseDetails = getCaseDetails();
        caseDetails.setState(Submitted);
        SubmittedCallbackResponse submittedCallbackResponse = localAuthoritySubmitApplication.submitted(caseDetails, caseDetails);
        System.out.println(submittedCallbackResponse.toString());
        assertThat(submittedCallbackResponse).isNotNull();
    }

    @Test
    void localAuthoritySubmitApplicationAboutToSubmit() {
        var caseDetails = getCaseDetails();
        when(submissionService.laSubmitApplication(caseDetails)).thenReturn(caseDetails);
        var result = localAuthoritySubmitApplication.aboutToSubmit(caseDetails,caseDetails);
        assertThat(result.getState()).isEqualTo(LaSubmitted);
    }

    private CaseDetails<CaseData, State> getCaseDetails() {
        return CaseDetails.<CaseData, State>builder()
            .data(caseData())
            .id(1L)
            .build();
    }
}
