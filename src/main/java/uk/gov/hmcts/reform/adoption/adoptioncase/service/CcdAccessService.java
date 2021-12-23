package uk.gov.hmcts.reform.adoption.adoptioncase.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.ccd.client.CaseAssignmentApi;
import uk.gov.hmcts.reform.ccd.client.CaseUserApi;
import uk.gov.hmcts.reform.ccd.client.model.CaseAssignmentUserRoleWithOrganisation;
import uk.gov.hmcts.reform.ccd.client.model.CaseAssignmentUserRolesRequest;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.idam.client.models.User;

import java.util.List;
import java.util.Set;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.CASE_WORKER;

@Service
@Slf4j
public class CcdAccessService {
    @Autowired
    public CaseUserApi caseUserApi;

    @Autowired
    private CaseAssignmentApi caseAssignmentApi;

    @Autowired
    private IdamService idamService;

    @Autowired
    private AuthTokenGenerator authTokenGenerator;

    public void addApplicantSolicitorRole(String solicitorIdamToken, Long caseId) {
        User solicitorUser = idamService.retrieveUser(solicitorIdamToken);
        User systemUpdateUser = idamService.retrieveSystemUpdateUserDetails();

        Set<String> caseRoles = Set.of(CASE_WORKER.getRole());//TODO

        String solicitorUserId = solicitorUser.getUserDetails().getId();

        log.info(
            "Adding roles {} to case Id {} and user Id {}",
            caseRoles,
            caseId,
            solicitorUserId
        );

        String idamToken = systemUpdateUser.getAuthToken();
        String s2sToken = authTokenGenerator.generate();

        caseAssignmentApi.removeCaseUserRoles(
            idamToken,
            s2sToken,
            CaseAssignmentUserRolesRequest
                .builder()
                .caseAssignmentUserRolesWithOrganisation(
                    List.of(
                        CaseAssignmentUserRoleWithOrganisation.builder()
                            .caseDataId(caseId.toString())
                            .caseRole(CASE_WORKER.getRole())
                            .userId(solicitorUserId)
                            .build()
                    )
                )
                .build()
        );

        caseAssignmentApi.addCaseUserRoles(
            idamToken,
            s2sToken,
            CaseAssignmentUserRolesRequest
                .builder()
                .caseAssignmentUserRolesWithOrganisation(
                    List.of(
                        CaseAssignmentUserRoleWithOrganisation.builder()
                            .caseDataId(caseId.toString())
                            .caseRole(CASE_WORKER.getRole())
                            .userId(solicitorUserId)
                            .build()
                    )
                )
                .build()
        );

        log.info("Successfully added the applicant's roles to case Id {} ", caseId);
    }
}
