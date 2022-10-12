package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

import static uk.gov.hmcts.reform.adoption.adoptioncase.event.CaseWorkerAllocateJudge.ALLOCATE_JUDGE;


public class AddJudge implements CcdPageConfiguration {

    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder.page("pageAllocateJudge")
            .pageLabel(ALLOCATE_JUDGE)
            .label("headingLabel","## Choose a judge for this case")
            .label("subheadingLabel",
                   "Choose whether the placement judge is presiding or a new judge is being allocated.")
            .mandatory(CaseData::getAllocatedJudge, null,"Name of the judge")
            .done();
    }
}
