package uk.gov.hmcts.reform.adoption.service.task;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    ApplicationEventPublisher applicationEventPublisher;
    @InjectMocks
    EventService eventService;

    @Test
    void publishEventTest() {
        Object event = new Object();
        eventService.publishEvent(event);
        verify(applicationEventPublisher).publishEvent(event);
    }
}
