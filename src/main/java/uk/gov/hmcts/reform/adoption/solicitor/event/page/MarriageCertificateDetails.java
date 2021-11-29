package uk.gov.hmcts.reform.adoption.solicitor.event.page;

import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Application;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MarriageDetails;

public class MarriageCertificateDetails implements CcdPageConfiguration {

    @Override
    public void addTo(final PageBuilder pageBuilder) {

        pageBuilder
            .page("MarriageCertificateDetails")
            .pageLabel("Marriage certificate details")
            .complex(CaseData::getApplication)
                .complex(Application::getMarriageDetails)
                    .mandatory(MarriageDetails::getDate)
                    .mandatory(MarriageDetails::getApplicant1Name)
                    .mandatory(MarriageDetails::getApplicant2Name)
                    .mandatoryWithLabel(MarriageDetails::getMarriedInUk, "Did the marriage take place in the UK?")
                    .mandatory(MarriageDetails::getPlaceOfMarriage, "marriageMarriedInUk=\"No\"")
                    .mandatory(MarriageDetails::getCountryOfMarriage, "marriageMarriedInUk=\"No\"")
                .done()
            .done();
    }
}
