package uk.gov.hmcts.reform.adoption.adoptioncase.schedule;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.adoption.adoptioncase.service.CcdSearchService;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.adoption.notification.NotificationDispatcher;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;

@ExtendWith(SpringExtension.class)
class AlertDraftCaseApplicantBeforeDeletionTaskTest {

    @InjectMocks
    private AlertDraftCaseApplicantBeforeDeletionTask alertDraftCaseApplicantBeforeDeletionTask;

    @Mock
    private CcdSearchService ccdSearchService;

    @Mock
    private IdamService idamService;

    @Mock
    private AuthTokenGenerator authTokenGenerator;

    @Mock
    private NotificationDispatcher notificationDispatcher;

    @Test
    void alertDraftCaseApplicantBeforeDeletionTaskTest_run() {

        alertDraftCaseApplicantBeforeDeletionTask.run();

    }
}
