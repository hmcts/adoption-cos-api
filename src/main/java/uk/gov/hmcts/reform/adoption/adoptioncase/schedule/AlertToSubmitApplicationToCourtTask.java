package uk.gov.hmcts.reform.adoption.adoptioncase.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.adoption.adoptioncase.service.CcdSearchService;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.adoption.notification.DraftApplicationExpiringNotification;
import uk.gov.hmcts.reform.adoption.systemupdate.CaseDetailsConverter;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;

@Component
@Slf4j
@RequiredArgsConstructor
public class AlertToSubmitApplicationToCourtTask implements Runnable {


    @Autowired
    private CcdSearchService ccdSearchService;

    @Autowired
    private IdamService idamService;

    @Autowired
    private AuthTokenGenerator authTokenGenerator;

    @Autowired
    private DraftApplicationExpiringNotification draftApplicationExpiringNotification;

    @Autowired
    private CaseDetailsConverter caseDetailsConverter;

    @Value("${cron.alertDraftApplicant.offsetDays:69}")
    public  int emailAlertOffsetDays;


    /**
     * When an objectclear implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     *
     * <p>The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

        log.info("AlertLAToSubmitApplicationToCourtTask is firing");

    }

}
