package uk.gov.hmcts.reform.adoption.caseworker.service.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Applicant;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Solicitor;
import uk.gov.hmcts.reform.adoption.notification.CommonContent;
import uk.gov.hmcts.reform.adoption.notification.NotificationService;

import java.util.Map;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference.ENGLISH;
import static uk.gov.hmcts.reform.adoption.notification.CommonContent.SOLICITOR_NAME;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.APPLICANT_NOTICE_OF_PROCEEDINGS;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.APPLICANT_SOLICITOR_NOTICE_OF_PROCEEDINGS;

@Component
@Slf4j
public class NoticeOfProceedingsNotification {

    public static final String CASE_ID = "case id";
    public static final String SOLICITOR_ORGANISATION = "solicitor organisation";


    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CommonContent commonContent;

    public void send(final CaseData caseData, final Long caseId) {

        final Applicant applicant = caseData.getApplicant1();
        final Solicitor applicantSolicitor = applicant.getSolicitor();

        if (applicant.isRepresented()) {

            log.info("Sending Notice Of Proceedings email to applicant solicitor.  Case ID: {}", caseId);

            notificationService.sendEmail(
                applicantSolicitor.getEmail(),
                APPLICANT_SOLICITOR_NOTICE_OF_PROCEEDINGS,
                solicitorNoticeOfProceedingsTemplateVars(caseData, caseId),
                ENGLISH);
        } else {

            log.info("Sending Notice Of Proceedings email to applicant.  Case ID: {}", caseId);

            notificationService.sendEmail(
                applicant.getEmail(),
                APPLICANT_NOTICE_OF_PROCEEDINGS,
                commonContent.basicTemplateVars(caseData, caseId),
                applicant.getLanguagePreference());
        }
    }

    private Map<String, String> solicitorNoticeOfProceedingsTemplateVars(final CaseData caseData, final Long caseId) {
        final Map<String, String> templateVars = commonContent.basicTemplateVars(caseData, caseId);
        templateVars.put(SOLICITOR_NAME, caseData.getApplicant1().getSolicitor().getName());
        templateVars.put(CASE_ID, caseId.toString());
        return templateVars;
    }
}
