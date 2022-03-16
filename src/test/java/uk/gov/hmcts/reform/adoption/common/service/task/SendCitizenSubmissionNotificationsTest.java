package uk.gov.hmcts.reform.adoption.common.service.task;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.notification.ApplicationSubmittedNotification;
import uk.gov.hmcts.reform.adoption.notification.NotificationDispatcher;
import uk.gov.service.notify.NotificationClientException;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AwaitingPayment;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_CASE_ID;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(MockitoExtension.class)
class SendCitizenSubmissionNotificationsTest {

    @Mock
    private ApplicationSubmittedNotification applicationSubmittedNotification;

    @Mock
    private NotificationDispatcher notificationDispatcher;

    @InjectMocks
    private SendCitizenSubmissionNotifications sendCitizenSubmissionNotifications;

    @Test
    void shouldDispatchSubmittedNotifications() throws NotificationClientException, IOException {
        //Given
        final CaseData caseData = caseData();
        final CaseDetails<CaseData, State> caseDetails = new CaseDetails<>();
        caseDetails.setId(TEST_CASE_ID);
        caseDetails.setData(caseData);
        caseDetails.setState(State.Submitted);
        //When
        sendCitizenSubmissionNotifications.apply(caseDetails);
        //Then
        verify(notificationDispatcher).send(applicationSubmittedNotification, caseData, TEST_CASE_ID);
    }

    @Test
    void shouldNotDispatchSubmittedNotificationsIfOtherState() {
        //Given
        final CaseData caseData = caseData();
        final CaseDetails<CaseData, State> caseDetails = new CaseDetails<>();
        caseDetails.setId(TEST_CASE_ID);
        caseDetails.setData(caseData);
        caseDetails.setState(AwaitingPayment);
        //When
        sendCitizenSubmissionNotifications.apply(caseDetails);
        //Then
        verifyNoMoreInteractions(notificationDispatcher);
    }
}
