package uk.gov.hmcts.reform.adoption.adoptioncase.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionDocument;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MigrateCaseService {

    public void doCaseIdCheck(long caseId, long expectedCaseId, String migrationId) throws AssertionError {
        if (caseId != expectedCaseId) {
            throw new AssertionError("Migration {id = %s, case reference = %s}, expected case id %d".formatted(
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
            throw new AssertionError("Migration {id = %s, case reference = %s}, invalid hearing order bundle draft".formatted(
                migrationId, caseId));
        }

        return laDocumentsUploaded;
    }
}
