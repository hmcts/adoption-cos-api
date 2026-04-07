package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.SocialWorker;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

public class ManageAccessCodes implements CcdPageConfiguration {
    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder.page("manageAccessCodes")
            .pageLabel("Manage Social Worker Access Codes Test")
            .label("ManageChildSwLabel","## Child Social Worker Details")
            .complex(CaseData::getChildSocialWorker)
            .readonlyWithLabel(
                SocialWorker::getLocalAuthority, "Child SW LA"
            )
            .optionalWithLabel(
                SocialWorker::getLocalAuthorityEmail, "Child SW LA Email"
            )
            .optionalWithLabel(
                SocialWorker::getSocialWorkerEmail, "Child SW SW Email"
            )
            .done()
            .label("ManageApplicantSwLabel","## Applicant Social Worker Details")
            .complex(CaseData::getApplicantSocialWorker)
            .readonlyWithLabel(
                SocialWorker::getLocalAuthority, "App SW LA"
            )
            .optionalWithLabel(
                SocialWorker::getLocalAuthorityEmail, "App SW LA Email"
            )
            .optionalWithLabel(
                SocialWorker::getSocialWorkerEmail, "App SW SW Email"
            );
    }
}
