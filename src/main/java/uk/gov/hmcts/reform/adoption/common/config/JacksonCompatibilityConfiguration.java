package uk.gov.hmcts.reform.adoption.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonCompatibilityConfiguration {

    /**
     * Bridge bean for libraries compiled against Jackson 2 (e.g. ccd-config-generator).
     * Remove once those dependencies are upgraded to Jackson 3 (tools.jackson.*).
     */
    @Bean
    public com.fasterxml.jackson.databind.ObjectMapper jackson2ObjectMapper() {
        return new com.fasterxml.jackson.databind.ObjectMapper()
            .findAndRegisterModules();
    }
}
