package uk.gov.hmcts.reform.adoption.adoptioncase.common;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MessageDocumentList;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionUploadDocument;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.adoption.adoptioncase.common.CaseEventCommonMethods.prepareDocumentList;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

public class CaseEventCommonMethodsTest {



    @Test
    public void verifyMessageDocumentList_OK() {
        var caseData = getCaseDetails().getData();
        List<ListValue<AdoptionUploadDocument>> applicationDocumentCategory = new ArrayList<>();
        caseData.setApplicationDocumentsCategory(caseData.archiveManageOrdersHelper(applicationDocumentCategory,
                                                                                    getApplicationDocumentCategory()));

        List<ListValue<AdoptionUploadDocument>> correspondenceDocCategory = new ArrayList<>();
        caseData.setCorrespondenceDocumentCategory(caseData.archiveManageOrdersHelper(correspondenceDocCategory,
                                                                                      getCorrespondanceDocumentCategory()));

        List<ListValue<AdoptionUploadDocument>> reportsDocumentCategory = new ArrayList<>();
        caseData.setReportsDocumentCategory(caseData.archiveManageOrdersHelper(reportsDocumentCategory,
                                                                                      getReportsDocumentCategory()));

        List<ListValue<AdoptionUploadDocument>> statementDocumentCategory= new ArrayList<>();
        caseData.setStatementsDocumentCategory(caseData.archiveManageOrdersHelper(statementDocumentCategory,
                                                                                      getStatementsDocumentCategory()));

        List<ListValue<AdoptionUploadDocument>> courtOrderCategory = new ArrayList<>();
        caseData.setCourtOrdersDocumentCategory(caseData.archiveManageOrdersHelper(courtOrderCategory,
                                                                                      getCourtOrdersDocumentCategory()));

        List<ListValue<AdoptionUploadDocument>> additionalDocumentCategory = new ArrayList<>();
        caseData.setAdditionalDocumentsCategory(caseData.archiveManageOrdersHelper(additionalDocumentCategory,
                                                                                      getAdditionalDocumentsCategory()));

        List<MessageDocumentList> list = prepareDocumentList(caseData);
        assertThat(list).hasSize(6);
        assertThat(list).isNotEmpty();
    }


    @Test
    public void verifyMessageDocumentList_recordNotFound() {
        List<MessageDocumentList> list = prepareDocumentList(getCaseDetails().getData());
        assertThat(list).hasSize(0);
        assertThat(list).isEmpty();
    }

    private AdoptionUploadDocument getApplicationDocumentCategory() {
        var uploadDocument = new AdoptionUploadDocument();
        uploadDocument.setName("testdoc.jpg");
        uploadDocument.setDocumentLink(new Document());
        return  uploadDocument;
    }

    private AdoptionUploadDocument getReportsDocumentCategory() {
        var uploadDocument = new AdoptionUploadDocument();
        uploadDocument.setName("testdoc1.jpg");
        uploadDocument.setDocumentLink(new Document());
        return  uploadDocument;
    }


    private AdoptionUploadDocument getStatementsDocumentCategory() {
        var uploadDocument = new AdoptionUploadDocument();
        uploadDocument.setName("testdoc2.jpg");
        uploadDocument.setDocumentLink(new Document());
        return  uploadDocument;
    }


    private AdoptionUploadDocument getCourtOrdersDocumentCategory() {
        var uploadDocument = new AdoptionUploadDocument();
        uploadDocument.setName("testdoc3.jpg");
        uploadDocument.setDocumentLink(new Document());
        return  uploadDocument;
    }


    private AdoptionUploadDocument getCorrespondanceDocumentCategory() {
        var uploadDocument = new AdoptionUploadDocument();
        uploadDocument.setName("testdoc4.jpg");
        uploadDocument.setDocumentLink(new Document());
        return  uploadDocument;
    }

    private AdoptionUploadDocument getAdditionalDocumentsCategory() {
        var uploadDocument = new AdoptionUploadDocument();
        uploadDocument.setName("testdoc5.jpg");
        uploadDocument.setDocumentLink(new Document());
        return  uploadDocument;
    }

    private CaseDetails<CaseData, State> getCaseDetails() {
        final var details = new CaseDetails<CaseData, State>();
        final var data = caseData();
        details.setData(data);
        details.setId(1L);
        return details;
    }
}
