package uk.gov.hmcts.reform.adoption.bulkscan.ccd.event;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.bulkscan.ccd.ExceptionRecordPageBuilder;
import uk.gov.hmcts.reform.adoption.bulkscan.ccd.ExceptionRecordState;
import uk.gov.hmcts.reform.adoption.bulkscan.data.ExceptionRecord;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.CASE_WORKER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.CASE_WORKER_BULK_SCAN;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SYSTEM_UPDATE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions.CREATE_READ_UPDATE;
import static uk.gov.hmcts.reform.adoption.bulkscan.ccd.ExceptionRecordState.ScannedRecordAttachedToCase;
import static uk.gov.hmcts.reform.adoption.bulkscan.ccd.ExceptionRecordState.ScannedRecordReceived;

@Component
public class AttachExceptionRecordToCase implements CCDConfig<ExceptionRecord, ExceptionRecordState, UserRole> {

    public static final String ATTACH_TO_EXISTING_CASE = "attachToExistingCase";

    @Override
    public void configure(final ConfigBuilder<ExceptionRecord, ExceptionRecordState, UserRole> configBuilder) {
        new ExceptionRecordPageBuilder(configBuilder
                                           .event(ATTACH_TO_EXISTING_CASE)
                                           .forStateTransition(ScannedRecordReceived, ScannedRecordAttachedToCase)
                                           .name("Attach record to existing case")
                                           .description("Attach record to existing case")
                                           .showEventNotes()
                                           .grant(CREATE_READ_UPDATE, CASE_WORKER_BULK_SCAN, CASE_WORKER, SYSTEM_UPDATE))
            .page("attachToExistingCase")
            .pageLabel("Correspondence")
            .readonlyNoSummary(ExceptionRecord::getShowEnvelopeCaseReference,"envelopeCaseReference=\"ALWAYS_HIDE\"")
            .readonlyNoSummary(ExceptionRecord::getShowEnvelopeLegacyCaseReference,"envelopeLegacyCaseReference=\"ALWAYS_HIDE\"")
            .readonly(ExceptionRecord::getEnvelopeCaseReference,"showEnvelopeCaseReference=\"Yes\"")
            .readonly(ExceptionRecord::getShowEnvelopeLegacyCaseReference,"showEnvelopeLegacyCaseReference=\"Yes\"")
            .mandatory(ExceptionRecord::getSearchCaseReference)
            .mandatory(ExceptionRecord::getScannedDocuments);
    }
}
