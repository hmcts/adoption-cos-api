package uk.gov.hmcts.reform.adoption.adoptioncase.service;

import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionDocument;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith({MockitoExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
public class MigrateCaseServiceTest {

    private static final String MIGRATION_ID = "test-migration";

    @InjectMocks
    private MigrateCaseService underTest;

    @Nested
    class RemoveLaDocumentsUploadedBundle {

        private final String bundleIdToRemove = UUID.randomUUID().toString();
        private final String bundleIdToKeep = UUID.randomUUID().toString();

        ListValue<AdoptionDocument> bundleToRemove = new ListValue<>();
        ListValue<AdoptionDocument> bundleToKeep = new ListValue<>();

        @Test
        void shouldClearLaDocumentsUploadedBundlesWithNoOrderPostMigration() {
            bundleToRemove.setId(bundleIdToRemove);
            bundleToRemove.setValue(AdoptionDocument.builder().build());

            CaseData caseData = CaseData.builder()
                .laDocumentsUploaded(List.of(bundleToRemove))
                .build();

            List<ListValue<AdoptionDocument>> laDocList = underTest.removeLaDocumentsUploadedBundleByID(caseData, MIGRATION_ID, bundleIdToRemove);

            assertThat(laDocList).isEqualTo(List.of());
        }

        @Test
        void shouldLeaveOtherBundlesIntact() {
            bundleToRemove.setId(bundleIdToRemove);
            bundleToKeep.setId(bundleIdToKeep);
            bundleToRemove.setValue(AdoptionDocument.builder().build());
            bundleToKeep.setValue(AdoptionDocument.builder().build());

            CaseData caseData = CaseData.builder()
                .laDocumentsUploaded(List.of(bundleToRemove, bundleToKeep))
                .build();

            List<ListValue<AdoptionDocument>> laDocList = underTest.removeLaDocumentsUploadedBundleByID(caseData, MIGRATION_ID, bundleIdToRemove);

            assertThat(laDocList).isEqualTo(List.of(bundleToKeep));
        }

        @Test
        void shouldThrowExceptionIfNoOrderFound() {
            bundleToRemove.setId(bundleIdToRemove);
            bundleToRemove.setValue(AdoptionDocument.builder().build());

            CaseData caseData = CaseData.builder()
                .laDocumentsUploaded(List.of())
                .build();

            assertThrows(AssertionError.class, () ->
                underTest.removeLaDocumentsUploadedBundleByID(caseData, MIGRATION_ID,
                    bundleIdToRemove));
        }
    }
}
