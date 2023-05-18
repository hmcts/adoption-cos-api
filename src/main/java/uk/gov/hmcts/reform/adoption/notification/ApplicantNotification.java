package uk.gov.hmcts.reform.adoption.notification;

import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.service.notify.NotificationClientException;

import java.io.IOException;

public interface ApplicantNotification {
    default void sendToApplicants(final CaseData caseData, final Long caseId) {
        //No operation
    }

    default void sendToCaseWorker(final CaseData caseData, final Long caseId) {
        //No operation
    }

    default void sendToLocalAuthorityPostApplicantSubmission(final CaseData caseData, final Long caseId) {
        //No operation
    }

    default void sendToLocalCourt(CaseData caseData, Long id) throws NotificationClientException, IOException {
        //No operation
    }

    default void sendToLocalAuthorityPostLocalAuthoritySubmission(final CaseData caseData, final Long caseId) {
        //No operation
    }

    default void sendToApplicantsPostLocalAuthoritySubmission(CaseData caseData, Long caseId){

    }

    default void sendToLocalCourtPostLocalAuthoritySubmission(CaseData caseData, Long id) throws NotificationClientException, IOException {
        //No operation
    }
}
