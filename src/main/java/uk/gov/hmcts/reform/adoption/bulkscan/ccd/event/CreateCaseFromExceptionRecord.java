package uk.gov.hmcts.reform.adoption.bulkscan.ccd.event;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.reform.adoption.bulkscan.data.ExceptionRecord;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.bulkscan.ccd.ExceptionRecordPageBuilder;
import uk.gov.hmcts.reform.adoption.bulkscan.ccd.ExceptionRecordState;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.ADOPTION_GENERIC;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.CASE_WORKER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.CASE_WORKER_BULK_SCAN;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions.CREATE_READ_UPDATE;
import static uk.gov.hmcts.reform.adoption.bulkscan.ccd.ExceptionRecordState.ScannedRecordCaseCreated;
import static uk.gov.hmcts.reform.adoption.bulkscan.ccd.ExceptionRecordState.ScannedRecordReceived;

@Component
public class CreateCaseFromExceptionRecord implements CCDConfig<ExceptionRecord, ExceptionRecordState, UserRole> {

    public static final String CREATE_NEW_CASE = "createNewCase";

    @Override
    public void configure(final ConfigBuilder<ExceptionRecord, ExceptionRecordState, UserRole> configBuilder) {
        new ExceptionRecordPageBuilder(configBuilder
                                           .event(CREATE_NEW_CASE)
                                           .forStateTransition(ScannedRecordReceived, ScannedRecordCaseCreated)
                                           .name("Create new case from exception")
                                           .description("Create new case from exception")
                                           .showEventNotes()
                                           .grant(CREATE_READ_UPDATE, CASE_WORKER_BULK_SCAN, CASE_WORKER, ADOPTION_GENERIC))
            .page("createNewCase")
            .pageLabel("Correspondence")
            .mandatory(ExceptionRecord::getScannedDocuments)
            .mandatory(ExceptionRecord::getScanOCRData);
    }
}
