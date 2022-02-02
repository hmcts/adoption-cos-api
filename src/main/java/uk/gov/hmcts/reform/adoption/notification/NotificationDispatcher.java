package uk.gov.hmcts.reform.adoption.notification;

import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;

@Service
public class NotificationDispatcher {
    public void send(final ApplicantNotification applicantNotification, final CaseData caseData, final Long caseId) {
        if (!caseData.getApplicant1().getEmailAddress().isEmpty()) {
            applicantNotification.sendToApplicant1(caseData, caseId);
        }
    }
}
