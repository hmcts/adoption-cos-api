package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.reform.adoption.adoptioncase.model.Applicant;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

public class RequestAnnexA implements CcdPageConfiguration {

    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder.page("requestAnnexA")
            .complex(CaseData::getApplicant1)
            .optionalWithLabel(
                Applicant::getEmailAddress,
                "Email")
            .optionalWithLabel(Applicant::getPhoneNumber,
                               "Phone number")
            .done();
    }
}

