package uk.gov.hmcts.reform.adoption.adoptioncase.bulkscan.ccd.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import uk.gov.hmcts.ccd.sdk.api.Event;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.bulkscan.ccd.ExceptionRecordState;
import uk.gov.hmcts.reform.adoption.bulkscan.ccd.event.AttachExceptionRecordToCase;
import uk.gov.hmcts.reform.adoption.bulkscan.data.ExceptionRecord;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.adoption.bulkscan.ccd.event.AttachExceptionRecordToCase.ATTACH_TO_EXISTING_CASE;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.createExceptionRecordConfigBuilder;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.getEventsFrom;

@ExtendWith(MockitoExtension.class)
class AttachExceptionRecordToCaseTest {

    @InjectMocks
    private AttachExceptionRecordToCase attachExceptionRecordToCase;

    @Test
    void shouldAddConfigurationToConfigBuilder() {
        final ConfigBuilderImpl<ExceptionRecord, ExceptionRecordState, UserRole> configBuilder = createExceptionRecordConfigBuilder();

        attachExceptionRecordToCase.configure(configBuilder);

        assertThat(getEventsFrom(configBuilder).values())
            .extracting(Event::getId)
            .contains(ATTACH_TO_EXISTING_CASE);
    }
}
