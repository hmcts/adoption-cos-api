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
import static uk.gov.hmcts.reform.adoption.bulkscan.ccd.ExceptionRecordState.ScannedRecordReceived;
import static uk.gov.hmcts.reform.adoption.bulkscan.ccd.ExceptionRecordState.ScannedRecordRejected;

@Component
public class RejectExceptionRecord implements CCDConfig<ExceptionRecord, ExceptionRecordState, UserRole> {

    public static final String REJECT_RECORD = "rejectRecord";

    @Override
    public void configure(final ConfigBuilder<ExceptionRecord, ExceptionRecordState, UserRole> configBuilder) {
        new ExceptionRecordPageBuilder(configBuilder
                                           .event(REJECT_RECORD)
                                           .forStateTransition(ScannedRecordReceived, ScannedRecordRejected)
                                           .name("Reject record")
                                           .description("Reject record")
                                           .showEventNotes()
                                           .grant(CREATE_READ_UPDATE, CASE_WORKER_BULK_SCAN, CASE_WORKER, SYSTEM_UPDATE))
            .page("createNewCase")
            .pageLabel("Correspondence")
            .mandatory(ExceptionRecord::getScannedDocuments);
    }
}
