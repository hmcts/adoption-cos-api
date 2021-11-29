package uk.gov.hmcts.reform.adoption.bulkaction.task;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.adoption.bulkaction.data.BulkActionCaseData;
import uk.gov.hmcts.reform.adoption.divorcecase.task.CaseTask;

import static uk.gov.hmcts.reform.adoption.systemupdate.event.SystemUpdateCaseWithPronouncementJudge.SYSTEM_UPDATE_CASE_PRONOUNCEMENT_JUDGE;

@Component
public class UpdateCasePronouncementJudgeProvider implements BulkActionCaseTaskProvider {

    @Override
    public String getEventId() {
        return SYSTEM_UPDATE_CASE_PRONOUNCEMENT_JUDGE;
    }

    @Override
    public CaseTask getCaseTask(final BulkActionCaseData bulkActionCaseData) {
        return mainCaseDetails -> {
            final var conditionalOrder = mainCaseDetails.getData().getConditionalOrder();
            conditionalOrder.setPronouncementJudge(
                bulkActionCaseData.getPronouncementJudge()
            );
            return mainCaseDetails;
        };
    }
}
