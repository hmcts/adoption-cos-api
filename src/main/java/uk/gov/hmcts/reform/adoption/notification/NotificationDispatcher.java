package uk.gov.hmcts.reform.adoption.notification;

import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.service.notify.NotificationClientException;

import java.io.IOException;

@Service
public class NotificationDispatcher {
    public void send(final ApplicantNotification applicantNotification, final CaseData caseData, final Long caseId)
        throws NotificationClientException, IOException {
        if (!caseData.getApplicant1().getEmailAddress().isEmpty()) {
            applicantNotification.sendToApplicants(caseData, caseId);
            applicantNotification.sendToLocalCourt(caseData, caseId);
        }
    }
}
