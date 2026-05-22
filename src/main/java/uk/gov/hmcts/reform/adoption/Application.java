package uk.gov.hmcts.reform.adoption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import uk.gov.hmcts.reform.adoption.document.CaseDocumentClient;
import uk.gov.hmcts.reform.adoption.document.DocAssemblyClient;
import uk.gov.hmcts.reform.adoption.service.task.ScheduledTaskRunner;
import uk.gov.hmcts.reform.authorisation.ServiceAuthorisationApi;
import uk.gov.hmcts.reform.ccd.client.CaseAssignmentApi;
import uk.gov.hmcts.reform.ccd.client.CaseUserApi;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataApi;
import uk.gov.hmcts.reform.idam.client.IdamApi;

/* 

This change is a temporary compatibility patch, the use of a compatability bean: 
adoption-cos-api/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports, 
so the app can run on the newer Spring Boot version.

Spring Boot 4 changed how it discovers “automatic setup” from libraries. ccd-case-document-am-client 
tries to load an outdated health-check class, which crashes startup. So the PR does three things to avoid that crash:

 1. Application.java: removes direct wiring to the old client class/package from that library.
 2. AutoConfiguration.imports: adds the library’s auto-config class in Boot 4’s new format (so Spring can recognise it in the new system).
 3. application.yaml: explicitly excludes that auto-config class so it doesn’t run and break startup. 
 
 This will be resolved in ADOP-2838
 
 */


@SpringBootApplication(
    scanBasePackages = {
        "uk.gov.hmcts.ccd.sdk",
        "uk.gov.hmcts.reform.adoption",
        "uk.gov.hmcts.reform.idam"
    }
)
@EnableFeignClients(
    clients = {
        IdamApi.class,
        ServiceAuthorisationApi.class,
        CaseUserApi.class,
        DocAssemblyClient.class,
        CoreCaseDataApi.class,
        CaseAssignmentApi.class,
        CaseDocumentClient.class
    }
)
@EnableScheduling
@EnableRetry
@SuppressWarnings("HideUtilityClassConstructor")
@Slf4j
public class Application implements CommandLineRunner {

    @Autowired
    private ScheduledTaskRunner taskRunner;


    public static final String TASK_NAME = "TASK_NAME";


    public static void main(final String[] args) {
        final var application = new SpringApplication(Application.class);
        final var instance = application.run(args);

        if (System.getenv(TASK_NAME) != null) {
            instance.close();
        }
    }

    @Override
    public void run(String... args) {
        log.info("running tasks: " + args);
        if (System.getenv(TASK_NAME) != null) {
            taskRunner.run(System.getenv(TASK_NAME));
        }
    }


}
