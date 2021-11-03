package uk.gov.hmcts.reform.adoption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@SuppressWarnings("HideUtilityClassConstructor") // Spring needs a constructor, its not a utility class
@EnableSwagger2
@Slf4j
public class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
