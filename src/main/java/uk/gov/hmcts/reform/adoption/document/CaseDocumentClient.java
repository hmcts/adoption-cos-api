package uk.gov.hmcts.reform.adoption.document;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

import static uk.gov.hmcts.reform.adoption.common.config.ControllerConstants.SERVICE_AUTHORIZATION;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.DOCUMENT_ID;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.PERMANENT;

@FeignClient(name = "case-document-am-api", url = "${case_document_am.url}/cases/documents")
public interface CaseDocumentClient {


    @DeleteMapping(value = "/{documentId}")
    void deleteDocument(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorisation,
                        @RequestHeader(SERVICE_AUTHORIZATION) String serviceAuth,
                        @PathVariable(DOCUMENT_ID) UUID documentId,
                        @RequestParam(PERMANENT) boolean permanent);

    @GetMapping(value = "/{documentId}/binary")
    ResponseEntity<Resource> getDocumentBinary(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorisation,
                                               @RequestHeader(SERVICE_AUTHORIZATION) String serviceAuth,
                                               @PathVariable(DOCUMENT_ID) UUID documentId);
}
