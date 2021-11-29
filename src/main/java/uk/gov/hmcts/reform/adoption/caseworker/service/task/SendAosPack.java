package uk.gov.hmcts.reform.adoption.caseworker.service.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.adoption.caseworker.service.print.AosPackPrinter;
import uk.gov.hmcts.reform.adoption.divorcecase.model.Applicant;
import uk.gov.hmcts.reform.adoption.divorcecase.model.CaseData;
import uk.gov.hmcts.reform.adoption.divorcecase.model.Solicitor;
import uk.gov.hmcts.reform.adoption.divorcecase.model.State;
import uk.gov.hmcts.reform.adoption.divorcecase.task.CaseTask;

@Component
@Slf4j
public class SendAosPack implements CaseTask {

    @Autowired
    private AosPackPrinter aosPackPrinter;

    @Override
    public CaseDetails<CaseData, State> apply(final CaseDetails<CaseData, State> caseDetails) {

        final Long caseId = caseDetails.getId();
        final CaseData caseData = caseDetails.getData();

        if (caseDetails.getData().getApplication().isSolicitorApplication() && !caseData.getApplication().isSolicitorServiceMethod()) {

            final Applicant respondent = caseData.getApplicant2();
            final Solicitor respondentSolicitor = respondent.getSolicitor();

            if (respondent.isRepresented()) {
                log.info("Sending respondent AoS pack to bulk print, "
                    + "respondent is represented by digital solicitor.  Case ID: {}:", caseId);
                aosPackPrinter.print(caseData, caseId);

                log.info("Setting Notice Of Proceedings information. CaseID: {}", caseId);
                caseData.getAcknowledgementOfService().setNoticeOfProceedings(respondentSolicitor);
            } else {
                log.info("Sending respondent AoS pack to bulk print, respondent is not represented.  CaseID: {}", caseId);
                aosPackPrinter.print(caseData, caseId);
            }
        }

        return caseDetails;
    }
}
