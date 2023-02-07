package uk.gov.hmcts.reform.adoption.adoptioncase.schedule;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class AlertDraftCaseApplicantBeforeDeletionTaskTest {

    @InjectMocks
    private AlertDraftCaseApplicantBeforeDeletionTask alertDraftCaseApplicantBeforeDeletionTask;

    @Test
    void alertDraftCaseApplicantBeforeDeletionTaskTest_run() {

        alertDraftCaseApplicantBeforeDeletionTask.run();

    }
}
