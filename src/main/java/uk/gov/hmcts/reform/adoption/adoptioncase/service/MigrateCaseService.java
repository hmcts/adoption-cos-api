package uk.gov.hmcts.reform.adoption.adoptioncase.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionDocument;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MigrateCaseService {

    public void doCaseIdCheck(long caseId, long expectedCaseId, String migrationId) throws AssertionError {
        if (caseId != expectedCaseId) {
            throw new AssertionError(format(
                "Migration {id = %s, case reference = %s}, expected case id %d",
                migrationId, caseId, expectedCaseId
            ));
        }
    }

    public List<ListValue<AdoptionDocument>> removeLaDocumentsUploadedBundleByID(CaseData caseData,
                                                                String migrationId,
                                                                String expectedLaDocumentsUploadedBundleId) {
        String caseId = caseData.getHyphenatedCaseRef();
        List<ListValue<AdoptionDocument>> laDocumentsUploaded = caseData.getLaDocumentsUploaded()
            .stream().filter(lv -> !lv.getId().equals(expectedLaDocumentsUploadedBundleId)).toList();

        if (laDocumentsUploaded.size() != caseData.getLaDocumentsUploaded().size() - 1) {
            throw new AssertionError(format(
                "Migration {id = %s, case reference = %s}, invalid hearing order bundle draft",
                migrationId, caseId));
        }

        return laDocumentsUploaded;
    }
}
