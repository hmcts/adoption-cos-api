package uk.gov.hmcts.reform.adoption.document;

import feign.FeignException;
import feign.Request;
import feign.Response;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionDocument;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.idam.client.models.User;
import uk.gov.hmcts.reform.idam.client.models.UserDetails;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static feign.Request.HttpMethod.DELETE;
import static feign.Request.HttpMethod.GET;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.doThrow;
import static uk.gov.hmcts.reform.adoption.document.DocumentType.APPLICATION;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.SYSTEM_USER_USER_ID;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_SERVICE_AUTH_TOKEN;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_CASE_ID;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.documentWithType;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class DraftApplicationRemovalServiceTest {

    @Mock
    private CaseDocumentClient caseDocumentClient;

    @Mock
    private AuthTokenGenerator authTokenGenerator;

    @Mock
    private IdamService idamService;

    @InjectMocks
    private DraftApplicationRemovalService draftApplicationRemovalService;

    @Test
    void shouldRemoveDraftApplicationDocumentFromCaseDataAndDeleteApplicationDocumentFromDocManagement() {
        final List<String> systemRoles = List.of("caseworker-adoption");
        final String systemRolesCsv = String.join(",", systemRoles);
        final ListValue<AdoptionDocument> adoptionDocumentListValue = documentWithType(APPLICATION);
        final String userId = UUID.randomUUID().toString();
        final User systemUser = systemUser(systemRoles, userId);

        when(idamService.retrieveSystemUpdateUserDetails()).thenReturn(systemUser);
        when(authTokenGenerator.generate()).thenReturn(TEST_SERVICE_AUTH_TOKEN);

        final String documentUuid = FilenameUtils.getName(adoptionDocumentListValue.getValue().getDocumentLink().getUrl());

        doNothing().when(caseDocumentClient).deleteDocument(
            SYSTEM_USER_USER_ID,
            TEST_SERVICE_AUTH_TOKEN,
            UUID.fromString(documentUuid),
            true
        );

        final List<ListValue<AdoptionDocument>> actualDocumentsList = draftApplicationRemovalService.removeDraftApplicationDocument(
            singletonList(adoptionDocumentListValue),
            TEST_CASE_ID
        );

        verify(idamService).retrieveSystemUpdateUserDetails();
        verify(authTokenGenerator).generate();
        verify(caseDocumentClient).deleteDocument(
            SYSTEM_USER_USER_ID,
            TEST_SERVICE_AUTH_TOKEN,
            UUID.fromString(documentUuid),
            true
        );

        verifyNoMoreInteractions(idamService, authTokenGenerator, caseDocumentClient);
    }

    @Test
    void shouldThrow403ForbiddenWhenServiceIsNotWhitelistedInDocManagement() {
        final List<String> systemRoles = List.of("caseworker-adoption");
        final String userId = UUID.randomUUID().toString();
        final User systemUser = systemUser(systemRoles, userId);

        when(idamService.retrieveSystemUpdateUserDetails()).thenReturn(systemUser);
        when(authTokenGenerator.generate()).thenReturn(TEST_SERVICE_AUTH_TOKEN);

        final byte[] emptyBody = {};
        final Request request = Request.create(DELETE, EMPTY, Map.of(), emptyBody, UTF_8, null);

        final FeignException feignException = FeignException.errorStatus(
            "userRolesNotAllowedToDelete",
            Response.builder()
                .request(request)
                .status(403)
                .headers(emptyMap())
                .reason("User role is not authorised to delete document")
                .build()
        );

        doThrow(feignException)
            .when(caseDocumentClient)
            .deleteDocument(
                anyString(),
                anyString(),
                any(),
                anyBoolean()
            );

        assertThatThrownBy(() -> draftApplicationRemovalService.removeDraftApplicationDocument(
            singletonList(documentWithType(APPLICATION)),
            TEST_CASE_ID
        ))
            .hasMessageContaining("403 User role is not authorised to delete document")
            .isExactlyInstanceOf(FeignException.Forbidden.class);

        verify(idamService).retrieveSystemUpdateUserDetails();
        verify(authTokenGenerator).generate();
        verifyNoMoreInteractions(idamService, authTokenGenerator);
    }

    @Test
    void shouldThrow401UnAuthorizedWhenServiceAuthTokenGenerationFails() {
        final List<String> systemRoles = List.of("caseworker-adoption");
        final String userId = UUID.randomUUID().toString();
        final User systemUser = systemUser(systemRoles, userId);

        when(idamService.retrieveSystemUpdateUserDetails()).thenReturn(systemUser);

        final byte[] emptyBody = {};
        final Request request = Request.create(GET, EMPTY, Map.of(), emptyBody, UTF_8, null);

        final FeignException feignException = FeignException.errorStatus(
            "invalidSecret",
            Response.builder()
                .request(request)
                .status(401)
                .headers(emptyMap())
                .reason("Invalid s2s secret")
                .build()
        );

        doThrow(feignException).when(authTokenGenerator).generate();

        assertThatThrownBy(() -> draftApplicationRemovalService.removeDraftApplicationDocument(
            singletonList(documentWithType(APPLICATION)),
            TEST_CASE_ID
        ))
            .hasMessageContaining("401 Invalid s2s secret")
            .isExactlyInstanceOf(FeignException.Unauthorized.class);

        verify(idamService).retrieveSystemUpdateUserDetails();
        verifyNoMoreInteractions(idamService);
    }

    private User systemUser(final List<String> solicitorRoles, final String userId) {
        final UserDetails userDetails = UserDetails
            .builder()
            .roles(solicitorRoles)
            .id(userId)
            .build();

        return new User(SYSTEM_USER_USER_ID, userDetails);
    }
}
