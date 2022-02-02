package uk.gov.hmcts.reform.adoption.notification;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationDispatcherTest {

    @Spy
    private ApplicantNotification applicantNotification = new TestNotification();

    @InjectMocks
    private NotificationDispatcher notificationDispatcher;

    public static class TestNotification implements ApplicantNotification {
    }
}
