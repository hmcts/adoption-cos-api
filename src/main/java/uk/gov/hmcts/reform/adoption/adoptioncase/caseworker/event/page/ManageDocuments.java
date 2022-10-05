package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import lombok.extern.slf4j.Slf4j;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.document.DocumentSubmitter;
import uk.gov.hmcts.reform.adoption.document.model.AdoptionUploadDocument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.nonNull;
import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseworkerUploadDocument.MANAGE_DOCUMENT;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.BLANK_SPACE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.STRING_COLON;

/**
 * This class is used to display the Manage Documents screen having all mandatory fields.
 */
@Slf4j
public class ManageDocuments implements CcdPageConfiguration {

    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder.page("uploadDocumentPage1", this::midEvent)
            .pageLabel(MANAGE_DOCUMENT)
            .complex(CaseData::getAdoptionUploadDocument)
            .mandatory(AdoptionUploadDocument::getDocumentLink)
            .mandatory(AdoptionUploadDocument::getDocumentComment)
            .mandatory(AdoptionUploadDocument::getDocumentCategory)
            .done()
            .done();
        pageBuilder.page("uploadDocumentPage2")
            .pageLabel("Who submitted the document?")
            .label("uploadDocumentPage2Label","Who submitted the document?")
            .mandatory(CaseData::getDocumentSubmitter)
            .mandatory(CaseData::getName, "documentSubmitter=\"otherParty\"")
            .mandatory(CaseData::getRole)
            .done();

    }

    public AboutToStartOrSubmitResponse<CaseData, State> midEvent(
        final CaseDetails<CaseData, State> details,
        final CaseDetails<CaseData, State> detailsBefore) {

        CaseData caseData = details.getData();
        List<DynamicListElement> listElements = new ArrayList<>();

        //log.info("<<<<<<<<<<>>>>>>>>>>>"+String.valueOf(UUID.fromString("otherParty")));

        /*DynamicListElement dynamicListElement = DynamicListElement.builder()
            .label(DocumentSubmitter.BIRTH_FATHER.getLabel())
            .code(UUID.randomUUID())
            .build();

        DynamicListElement dynamicListElement2 = DynamicListElement.builder()
            .label(DocumentSubmitter.BIRTH_MOTHER.getLabel())
            .code(UUID.randomUUID())
            .build();*/

        //UUID code = UUID.randomUUID();
        //log.info("----CODE-----" + code);
        DynamicListElement childSocialWorker = DynamicListElement.builder()
            .label(String.join(BLANK_SPACE,DocumentSubmitter.CHILD_SOCIAL_WORKER.getLabel(),
                               STRING_COLON,
                               caseData.getChildSocialWorker().getSocialWorkerName()))
            .code(UUID.randomUUID())
            .build();

        listElements.add(childSocialWorker);
        log.info("childSocialWorker");

        DynamicListElement adoptionAgencyOrLocalAuthority = DynamicListElement.builder()
            .label(String.join(BLANK_SPACE,DocumentSubmitter.ADOPTION_AGENCY_OR_LOCAL_AUTHORITY.getLabel(),
                               STRING_COLON,
                               caseData.getLocalAuthority().getLocalAuthorityName()))
            .code(UUID.randomUUID())
            .build();

        listElements.add(adoptionAgencyOrLocalAuthority);
        log.info("adoptionAgencyOrLocalAuthority");

        if(caseData.getOtherAdopAgencyOrLA()!=null && caseData.getOtherAdopAgencyOrLA().getOtherAdopAgencyOrLaName()!=null){
            DynamicListElement otherAdoptionAgencyOrLocalAuthority = DynamicListElement.builder()
                .label(String.join(BLANK_SPACE,DocumentSubmitter.OTHER_ADOPTION_AGENCY_OR_LOCAL_AUTHORITY.getLabel(),
                                   STRING_COLON,
                                   caseData.getOtherAdopAgencyOrLA().getOtherAdopAgencyOrLaName()))
                .code(UUID.randomUUID())
                .build();

            listElements.add(otherAdoptionAgencyOrLocalAuthority);
            log.info("getOtherAdopAgencyOrLA");
        }

        DynamicListElement firstApplicant = DynamicListElement.builder()
            .label(String.join(BLANK_SPACE,DocumentSubmitter.FIRST_APPLICANT.getLabel(),
                               STRING_COLON,
                               caseData.getApplicant1().getFirstName(),
                               caseData.getApplicant1().getLastName()))
            .code(UUID.randomUUID())
            .build();

        listElements.add(firstApplicant);

        if(caseData.getApplicant2()!=null && caseData.getApplicant2().getFirstName()!=null && caseData.getApplicant2().getLastName()!=null){
            DynamicListElement secondApplicant = DynamicListElement.builder()
                .label(String.join(BLANK_SPACE,DocumentSubmitter.SECOND_APPLICANT.getLabel(),
                                   STRING_COLON,
                                   caseData.getApplicant2().getFirstName(),
                                   caseData.getApplicant2().getLastName()))
                .code(UUID.randomUUID())
                .build();

            listElements.add(secondApplicant);
        }

        if(caseData.getBirthMother()!=null && caseData.getBirthMother().getFirstName()!=null && caseData.getBirthMother().getLastName()!=null){
            DynamicListElement birthMother = DynamicListElement.builder()
                .label(String.join(BLANK_SPACE,DocumentSubmitter.BIRTH_MOTHER.getLabel(),
                                   STRING_COLON,
                                   caseData.getBirthMother().getFirstName(),
                                   caseData.getBirthMother().getLastName()))
                .code(UUID.randomUUID())
                .build();

            listElements.add(birthMother);
        }
        if(caseData.getBirthFather()!=null && caseData.getBirthFather().getFirstName()!=null && caseData.getBirthFather().getLastName()!=null) {
            DynamicListElement birthFather = DynamicListElement.builder()
                .label(String.join(BLANK_SPACE, DocumentSubmitter.BIRTH_FATHER.getLabel(),
                                   STRING_COLON,
                                   caseData.getBirthFather().getFirstName(),
                                   caseData.getBirthFather().getLastName()
                ))
                .code(UUID.randomUUID())
                .build();

            listElements.add(birthFather);
        }

        if(caseData.getOtherParent()!=null && caseData.getOtherParent().getFirstName()!=null && caseData.getOtherParent().getLastName()!=null) {
            DynamicListElement personWithParentalResponsibility = DynamicListElement.builder()
                .label(String.join(BLANK_SPACE, DocumentSubmitter.PERSON_WITH_PARENTAL_RESPONSIBILITY.getLabel(),
                                   STRING_COLON,
                                   caseData.getOtherParent().getFirstName(),
                                   caseData.getOtherParent().getLastName()
                ))
                .code(UUID.randomUUID())
                .build();

            listElements.add(personWithParentalResponsibility);
        }

        DynamicListElement otherParty = DynamicListElement.builder()
            .label(DocumentSubmitter.OTHER_PARTY.getLabel())
            .code(UUID.fromString("1111-2222-3333-4444-5555"))
            .build();

        listElements.add(otherParty);


        /*listElements.add(dynamicListElement);
        listElements.add(dynamicListElement2);*/

        /*DynamicList abc =new DynamicList();
        abc.setListItems(listElements);
        abc.setValue(DynamicListElement.EMPTY);*/
        //DynamicList.builder().listItems(listElements).value(DynamicListElement.EMPTY).build();

        //build options for field (Default Value & List Options), add to case data
        /*CaseData.CaseDataBuilder caseDataBuilder  = caseData.toBuilder();
        caseDataBuilder.adoptionUploadDocument(AdoptionUploadDocument.builder()
                                                   .documentSubmitter(abc)
                                                   .build());*/


        caseData.setDocumentSubmitter(DynamicList.builder().listItems(listElements).value(DynamicListElement.EMPTY).build());
        log.info("MidEvent Triggered");


        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .build();
    }
}
