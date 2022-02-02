package uk.gov.hmcts.reform.adoption.notification;

import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;

public interface ApplicantNotification {
    default void sendToApplicant1(final CaseData caseData, final Long caseId) {
        //No operation
    }
}
