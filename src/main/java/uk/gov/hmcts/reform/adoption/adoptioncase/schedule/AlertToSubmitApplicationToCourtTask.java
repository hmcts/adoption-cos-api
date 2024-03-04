package uk.gov.hmcts.reform.adoption.adoptioncase.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class AlertToSubmitApplicationToCourtTask implements Runnable {

    @Override
    public void run() {

        log.info("AlertLAToSubmitApplicationToCourtTask is firing");

    }

}
