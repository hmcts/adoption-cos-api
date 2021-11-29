package uk.gov.hmcts.reform.adoption.caseworker.service.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.adoption.citizen.notification.ApplicationIssuedNotification;
import uk.gov.hmcts.reform.adoption.divorcecase.model.CaseData;
import uk.gov.hmcts.reform.adoption.divorcecase.model.State;
import uk.gov.hmcts.reform.adoption.divorcecase.task.CaseTask;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static uk.gov.hmcts.reform.adoption.divorcecase.model.State.AwaitingAos;

@Component
public class SendApplicationIssueNotifications implements CaseTask {

    @Autowired
    private ApplicationIssuedNotification applicationIssuedNotification;

    @Override
    public CaseDetails<CaseData, State> apply(final CaseDetails<CaseData, State> caseDetails) {

        if (!caseDetails.getData().getApplication().isSolicitorApplication()) {

            final CaseData caseData = caseDetails.getData();
            final Long caseId = caseDetails.getId();

            if (caseData.getApplicationType().isSole()) {
                applicationIssuedNotification.sendToSoleApplicant1(caseData, caseId);
                if (isNotBlank(caseData.getApplicant2EmailAddress())) {
                    applicationIssuedNotification.sendToSoleRespondent(caseData, caseId);
                }
                if (caseDetails.getState() == AwaitingAos && caseData.getApplicant2().isBasedOverseas()) {
                    applicationIssuedNotification.notifyApplicantOfServiceToOverseasRespondent(caseData, caseId);
                }
            } else {
                applicationIssuedNotification.sendToJointApplicant1(caseData, caseId);
                if (isNotBlank(caseData.getApplicant2EmailAddress())) {
                    applicationIssuedNotification.sendToJointApplicant2(caseData, caseId);
                }
            }
        }

        return caseDetails;
    }
}
