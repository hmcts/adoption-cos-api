package uk.gov.hmcts.reform.adoption.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.service.notify.NotificationClient;

@Configuration
@Slf4j
public class NotificationsConfiguration {
    @Bean
    public NotificationClient notificationClient(
        @Value("${uk.gov.notify.api.key}") String apiKey,
        @Value("${uk.gov.notify.api.baseUrl}") final String baseUrl
    ) {

        log.info("uk.gov.notify.api.key", apiKey);
        return new NotificationClient(apiKey, baseUrl);
    }
}
