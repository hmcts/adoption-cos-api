package uk.gov.hmcts.reform.adoption.document.content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.adoption.common.config.DocmosisTemplatesConfig;
import uk.gov.hmcts.reform.adoption.document.model.LanguagePreference;

@Component
public class DocmosisTemplateProvider {

    @Autowired
    private DocmosisTemplatesConfig docmosisTemplatesConfig;

    public String templateNameFor(final String templateId, final LanguagePreference languagePreference) {
        return docmosisTemplatesConfig.getTemplates().get(languagePreference).get(templateId);
    }
}
