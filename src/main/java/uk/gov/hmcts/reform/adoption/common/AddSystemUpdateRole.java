package uk.gov.hmcts.reform.adoption.common;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

import java.util.ArrayList;
import java.util.List;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SYSTEM_UPDATE;

@Component
public class AddSystemUpdateRole {
    private static final String ENVIRONMENT_AAT = "aat";

    public List<UserRole> addIfConfiguredForEnvironment(List<UserRole> userRoles) {
        List<UserRole> existingRoles = new ArrayList<>(userRoles);
        String environment = System.getenv().getOrDefault("ENVIRONMENT", null);

        if (null != environment && environment.equalsIgnoreCase(ENVIRONMENT_AAT)) {
            existingRoles.add(SYSTEM_UPDATE);
        }

        return existingRoles;
    }

    public boolean isEnvironmentAat() {
        String environment = System.getenv().getOrDefault("ENVIRONMENT", null);
        return null != environment && environment.equalsIgnoreCase(ENVIRONMENT_AAT);
    }
}
