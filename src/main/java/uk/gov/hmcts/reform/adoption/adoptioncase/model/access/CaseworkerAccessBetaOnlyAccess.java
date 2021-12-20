package uk.gov.hmcts.reform.adoptions.dacase.model.access;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import uk.gov.hmcts.ccd.sdk.api.HasAccessControl;
import uk.gov.hmcts.ccd.sdk.api.HasRole;
import uk.gov.hmcts.ccd.sdk.api.Permission;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.CASE_WORKER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.DISTRICT_JUDGE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.LEGAL_ADVISOR;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SOLICITOR;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SUPER_USER;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.SYSTEM_UPDATE;

public class CaseworkerAccessBetaOnlyAccess implements HasAccessControl {

    @Override
    public SetMultimap<HasRole, Permission> getGrants() {
        SetMultimap<HasRole, Permission> grants = HashMultimap.create();
        grants.putAll(SOLICITOR, Permissions.READ);
        grants.putAll(SUPER_USER, Permissions.READ);
        grants.putAll(LEGAL_ADVISOR, Permissions.READ);
        grants.putAll(DISTRICT_JUDGE, Permissions.READ);

        grants.putAll(CASE_WORKER, Permissions.CREATE_READ_UPDATE);
        grants.putAll(SYSTEM_UPDATE, Permissions.CREATE_READ_UPDATE);
        return grants;
    }
}
