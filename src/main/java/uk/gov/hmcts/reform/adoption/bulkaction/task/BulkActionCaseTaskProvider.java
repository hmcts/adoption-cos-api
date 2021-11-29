package uk.gov.hmcts.reform.adoption.bulkaction.task;

import uk.gov.hmcts.reform.adoption.bulkaction.data.BulkActionCaseData;
import uk.gov.hmcts.reform.adoption.divorcecase.task.CaseTask;

public interface BulkActionCaseTaskProvider {

    String getEventId();

    CaseTask getCaseTask(final BulkActionCaseData bulkActionCaseData);
}
