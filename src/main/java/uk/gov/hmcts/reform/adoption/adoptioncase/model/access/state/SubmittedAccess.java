package uk.gov.hmcts.reform.adoption.adoptioncase.model.access.state;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import uk.gov.hmcts.ccd.sdk.api.HasAccessControl;
import uk.gov.hmcts.ccd.sdk.api.HasRole;
import uk.gov.hmcts.ccd.sdk.api.Permission;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.CASE_WORKER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.CITIZEN;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.CREATOR;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SYSTEM_UPDATE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.TTL_PROFILE;

public class SubmittedAccess implements HasAccessControl {

    @Override
    public SetMultimap<HasRole, Permission> getGrants() {
        SetMultimap<HasRole, Permission> grants = HashMultimap.create();
        grants.putAll(CREATOR, Permissions.READ);
        grants.putAll(CITIZEN, Permissions.READ);
        grants.putAll(CASE_WORKER, Permissions.CREATE_READ_UPDATE);
        grants.putAll(SYSTEM_UPDATE, Permissions.CREATE_READ_UPDATE);
        grants.putAll(TTL_PROFILE, Permissions.CREATE_READ_UPDATE);

        return grants;
    }
}
