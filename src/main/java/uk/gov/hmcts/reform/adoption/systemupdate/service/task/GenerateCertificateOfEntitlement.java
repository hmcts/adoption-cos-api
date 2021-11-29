package uk.gov.hmcts.reform.adoption.systemupdate.service.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.task.CaseTask;
import uk.gov.hmcts.reform.adoption.document.CaseDataDocumentService;
import uk.gov.hmcts.reform.adoption.document.content.CertificateOfEntitlementContent;
import uk.gov.hmcts.reform.adoption.document.model.DivorceDocument;

import java.time.Clock;
import java.time.LocalDateTime;

import static uk.gov.hmcts.reform.adoption.caseworker.service.task.util.FileNameUtil.formatDocumentName;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.CERTIFICATE_OF_ENTITLEMENT_NAME;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.CERTIFICATE_OF_ENTITLEMENT_TEMPLATE_ID;
import static uk.gov.hmcts.reform.adoption.document.model.DocumentType.CERTIFICATE_OF_ENTITLEMENT;

@Component
@Slf4j
public class GenerateCertificateOfEntitlement implements CaseTask {

    @Autowired
    private CaseDataDocumentService caseDataDocumentService;

    @Autowired
    private CertificateOfEntitlementContent certificateOfEntitlementContent;

    @Autowired
    private Clock clock;

    @Override
    public CaseDetails<CaseData, State> apply(final CaseDetails<CaseData, State> caseDetails) {

        final Long caseId = caseDetails.getId();
        final CaseData caseData = caseDetails.getData();

        log.info("Generating certificate of entitlement pdf for CaseID: {}", caseDetails.getId());

        final Document certificateOfEntitlement = caseDataDocumentService.renderDocument(
            certificateOfEntitlementContent.apply(caseData, caseId),
            caseId,
            CERTIFICATE_OF_ENTITLEMENT_TEMPLATE_ID,
            caseData.getApplicant1().getLanguagePreference(),
            formatDocumentName(caseId, CERTIFICATE_OF_ENTITLEMENT_NAME, LocalDateTime.now(clock))
        );

        final DivorceDocument coeDivorceDocument = DivorceDocument
            .builder()
            .documentLink(certificateOfEntitlement)
            .documentFileName(certificateOfEntitlement.getFilename())
            .documentType(CERTIFICATE_OF_ENTITLEMENT)
            .build();

        caseData.getConditionalOrder().setCertificateOfEntitlementDocument(coeDivorceDocument);

        return caseDetails;
    }
}
