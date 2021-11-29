package uk.gov.hmcts.reform.adoption.bulkaction.task;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.adoption.bulkaction.data.BulkActionCaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.task.CaseTask;

import java.util.Map;
import javax.annotation.Resource;

@Component
public class BulkCaseCaseTaskFactory {

    @Resource(name = "bulkActionCaseTaskProviders")
    private Map<String, BulkActionCaseTaskProvider> caseTaskProviders;

    public CaseTask getCaseTask(final BulkActionCaseData bulkActionCaseData, final String eventId) {
        if (caseTaskProviders.containsKey(eventId)) {
            return caseTaskProviders.get(eventId).getCaseTask(bulkActionCaseData);
        }

        throw new IllegalArgumentException(String.format("Cannot create CaseTask for Event Id: %s", eventId));
    }
}
