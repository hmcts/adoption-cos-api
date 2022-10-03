package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseWorkerAllocateJudge.ALLOCATE_JUDGE;

public class AddJudge implements CcdPageConfiguration {

    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder.page("pageAllocateJudge")
        .pageLabel(ALLOCATE_JUDGE)
        .label("headingLabel","## Choose a judge for this case")
        .label("subheadingLabel","Ideally the judge chosen would be the same judge involved in the placement order.")
        //.complex(CaseData::getAllocatedJudge)
        .mandatory(CaseData::getAllocatedJudge, null,"Name of the judge")
       // .mandatoryWithLabel(AllocateJudge::getJudgeName, "Name of the judge")
        .done();
    }
}
