package uk.gov.hmcts.reform.adoption;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.rse.ccd.lib.api.CFTLib;
import uk.gov.hmcts.rse.ccd.lib.api.CFTLibConfigurer;

import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class CftLibConfig implements CFTLibConfigurer {
    @Override
    public void configure(CFTLib lib) throws Exception {
        lib.createRoles(
            "caseworker-adoption",
            "caseworker-adoption-caseworker",
            "caseworker-adoption-courtadmin",
            "caseworker-adoption-la",
            "caseworker-adoption-judge",
            "caseworker-adoption-superuser",
            "caseworker-adoption-solicitor",
            "citizen"
        );

        var def = Files.readAllBytes(Path.of("build/ccd-config/ccd-A58-dev.xlsx"));
        lib.importDefinition(def);
    }
}
