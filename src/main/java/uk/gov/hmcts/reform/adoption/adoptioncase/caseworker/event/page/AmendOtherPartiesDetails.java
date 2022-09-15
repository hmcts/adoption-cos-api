package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Children;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

public class AmendOtherPartiesDetails implements CcdPageConfiguration {


    @Override
    public void addTo(PageBuilder pageBuilder) {


        pageBuilder.page("amendOtherParties")
            .complex(CaseData::getChildren)
            .mandatoryWithLabel(Children::getFirstName,"First names")
            .mandatoryWithLabel(Children::getLastName,"Last names")
            .mandatoryWithLabel(Children::getDateOfBirth,"Date of birth")
            .mandatoryWithLabel(Children::getSexAtBirth,"Sex of birth")
            .mandatoryWithLabel(Children::getNationality,"Nationality")
            .mandatoryWithLabel(Children::getFirstNameAfterAdoption,"First names after adoption")
            .mandatoryWithLabel(Children::getLastNameAfterAdoption,"Last names after adoption")
            .done();



    }
}
