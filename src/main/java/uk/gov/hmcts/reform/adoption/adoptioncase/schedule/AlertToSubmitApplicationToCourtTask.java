package uk.gov.hmcts.reform.adoption.adoptioncase.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j  //NOSONAR test code to check CRON fires
@RequiredArgsConstructor
public class AlertToSubmitApplicationToCourtTask implements Runnable {

    @Override
    public void run() { //NOSONAR test code to check CRON fires
        log.info("AlertLAToSubmitApplicationToCourtTask is firing"); //NOSONAR test code to check CRON fires
    }

}
