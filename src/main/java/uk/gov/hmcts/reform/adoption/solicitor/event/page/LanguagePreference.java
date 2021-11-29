package uk.gov.hmcts.reform.adoption.solicitor.event.page;

import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.divorcecase.model.Applicant;
import uk.gov.hmcts.reform.adoption.divorcecase.model.CaseData;

public class LanguagePreference implements CcdPageConfiguration {

    @Override
    public void addTo(final PageBuilder pageBuilder) {

        pageBuilder
            .page("langPref")
            .pageLabel("Select Language")
            .complex(CaseData::getApplicant1)
                .mandatory(Applicant::getLanguagePreferenceWelsh,
                    null,
                    null,
                    "Include a Welsh copy of all generated divorce documents for Applicant 1?",
                    "An English copy will always be included"
                )
                .done();
    }
}
