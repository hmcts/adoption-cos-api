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

@Component
public class CreateExceptionRecord implements CCDConfig<ExceptionRecord, ExceptionRecordState, UserRole> {

    public static final String CREATE_EXCEPTION = "createException";

    @Override
    public void configure(final ConfigBuilder<ExceptionRecord, ExceptionRecordState, UserRole> configBuilder) {
        new ExceptionRecordPageBuilder(configBuilder
                                           .event(CREATE_EXCEPTION)
                                           .initialState(ScannedRecordReceived)
                                           .name("Create an exception record")
                                           .description("Create an exception record")
                                           .showEventNotes()
                                           .grant(CREATE_READ_UPDATE, CASE_WORKER_BULK_SCAN, CASE_WORKER, SYSTEM_UPDATE))
            .page("createException")
            .pageLabel("Correspondence")
            .readonly(ExceptionRecord::getEnvelopeLabel)
            .optional(ExceptionRecord::getJourneyClassification)
            .optional(ExceptionRecord::getPoBox)
            .optional(ExceptionRecord::getPoBoxJurisdiction)
            .optional(ExceptionRecord::getDeliveryDate)
            .optional(ExceptionRecord::getOpeningDate)
            .optional(ExceptionRecord::getScannedDocuments)
            .optional(ExceptionRecord::getScanOcrData)
            .optional(ExceptionRecord::getFormType)
            .optional(ExceptionRecord::getEnvelopeCaseReference)
            .optional(ExceptionRecord::getEnvelopeLegacyCaseReference)
            .optional(ExceptionRecord::getShowEnvelopeCaseReference)
            .optional(ExceptionRecord::getShowEnvelopeLegacyCaseReference);
    }
}
