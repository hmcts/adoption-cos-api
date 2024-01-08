package uk.gov.hmcts.reform.adoption.notification.handlers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.ApplyingWith;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.common.service.SendNotificationService;
import uk.gov.hmcts.reform.adoption.service.event.LocalAuthorityApplicationSubmitNotificationEvent;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LocalAuthorityApplicationSubmittedNotificationEventHandlerTest {

    @Mock
    private SendNotificationService sendNotificationService;
    @InjectMocks
    LocalAuthorityApplicationSubmittedNotificationEventHandler localAuthorityApplicationSubmittedNotificationEventHandler;

    @Test
    void sendNotificationPostApplicationSubmissionTest() {
        CaseDetails<CaseData, State> caseDetails = CaseDetails.<CaseData, State>builder()
            .id(1234123412341234L)
            .data(CaseData.builder()
                      .applyingWith(ApplyingWith.ALONE)
                      .build())
            .build();
        LocalAuthorityApplicationSubmitNotificationEvent localAuthorityApplicationSubmitNotificationEvent =
            LocalAuthorityApplicationSubmitNotificationEvent.builder()
            .caseData(caseDetails)
            .build();
        Mockito.when(sendNotificationService.sendNotifications(Mockito.any())).thenReturn(caseDetails);
        localAuthorityApplicationSubmittedNotificationEventHandler
            .sendNotificationPostLocalAuthoritySubmission(localAuthorityApplicationSubmitNotificationEvent);
        verify(sendNotificationService,
               times(1)).sendNotifications(caseDetails);

    }
}
