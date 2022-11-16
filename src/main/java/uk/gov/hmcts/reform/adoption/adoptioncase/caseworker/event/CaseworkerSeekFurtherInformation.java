package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page.SeekFurtherInformation;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.AdoptionSeekFurtherInformation;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;
import uk.gov.hmcts.reform.adoption.document.DocumentSubmitter;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.util.CollectionUtils.isEmpty;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.BLANK_SPACE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants.STRING_COLON;

@Slf4j
@Component
public class CaseworkerSeekFurtherInformation implements CCDConfig<CaseData, State, UserRole> {

    public static final String CASEWORKER_SEEK_FURTHER_INFORMATION = "caseworker-seekfurther-information";

    public static final String SEEK_FURTHER_INFORMATION_HEADING = "Seek further information";

    private final CcdPageConfiguration seekFurtherInformation = new SeekFurtherInformation();

    @Autowired
    private Clock clock;

    @Override
    public void configure(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        log.info("Inside configure method for Event {}", CASEWORKER_SEEK_FURTHER_INFORMATION);
        var pageBuilder = addEventConfig(configBuilder);
        seekFurtherInformation.addTo(pageBuilder);
    }


    /**
     * Helper method to make custom changes to the CCD Config in order to add the event to respective Page Configuration.
     *
     * @param configBuilder - Base CCD Config Builder updated to add Event for Page
     * @return - PageBuilder updated to use on overridden method.
     */
    private PageBuilder addEventConfig(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.grant(State.Draft, Permissions.READ_UPDATE, UserRole.CASE_WORKER, UserRole.COURT_ADMIN,
                            UserRole.LEGAL_ADVISOR, UserRole.DISTRICT_JUDGE
        );
        return new PageBuilder(configBuilder
                                   .event(CASEWORKER_SEEK_FURTHER_INFORMATION)
                                   .forAllStates()
                                   .name(SEEK_FURTHER_INFORMATION_HEADING)
                                   .description(SEEK_FURTHER_INFORMATION_HEADING)
                                   .showSummary()
                                   .grant(Permissions.CREATE_READ_UPDATE, UserRole.CASE_WORKER)
                                   .grant(Permissions.CREATE_READ_UPDATE, UserRole.DISTRICT_JUDGE)
                                   .aboutToStartCallback(this::seekFurtherInformationData)
                                   .aboutToSubmitCallback(this::aboutToSubmit));
    }

    /**
     * Method to fetch Seek Further Information list.
     *
     * @param details is type of Case Data
     * @return will return about to submit response
     */
    public AboutToStartOrSubmitResponse<CaseData, State> seekFurtherInformationData(CaseDetails<CaseData, State> details) {
        CaseData caseData = details.getData();
        List<DynamicListElement> listElements = new ArrayList<>();

        DynamicListElement childSocialWorker = DynamicListElement.builder()
            .label(joinDynamicListLabel(DocumentSubmitter.CHILD_SOCIAL_WORKER,
             caseData.getChildSocialWorker().getSocialWorkerName()))
            .code(UUID.randomUUID())
            .build();

        listElements.add(childSocialWorker);
        log.info("childSocialWorker");

        DynamicListElement adoptionAgencyOrLocalAuthority = DynamicListElement.builder()
            .label(joinDynamicListLabel(DocumentSubmitter.ADOPTION_AGENCY_OR_LOCAL_AUTHORITY,
             caseData.getLocalAuthority().getLocalAuthorityName()))
            .code(UUID.randomUUID())
            .build();

        listElements.add(adoptionAgencyOrLocalAuthority);
        log.info("adoptionAgencyOrLocalAuthority");

        if (caseData.getOtherAdoptionAgencyOrLA() != null && caseData.getOtherAdoptionAgencyOrLA().getAgencyOrLaName() != null) {
            DynamicListElement otherAdoptionAgencyOrLocalAuthority = DynamicListElement.builder()
                .label(joinDynamicListLabel(DocumentSubmitter.OTHER_ADOPTION_AGENCY_OR_LOCAL_AUTHORITY,
                 caseData.getOtherAdoptionAgencyOrLA().getAgencyOrLaName()))
                .code(UUID.randomUUID())
                .build();

            listElements.add(otherAdoptionAgencyOrLocalAuthority);
            log.info("getOtherAdopAgencyOrLA");
        }

        DynamicListElement firstApplicant = DynamicListElement.builder()
            .label(joinDynamicListLabel(DocumentSubmitter.FIRST_APPLICANT,
             String.join(caseData.getApplicant1().getFirstName(), caseData.getApplicant1().getLastName())))
            .code(UUID.randomUUID())
            .build();

        listElements.add(firstApplicant);

        if (caseData.getApplicant2() != null && caseData.getApplicant2().getFirstName() != null
            && caseData.getApplicant2().getLastName() != null) {
            DynamicListElement secondApplicant = DynamicListElement.builder()
                .label(joinDynamicListLabel(DocumentSubmitter.SECOND_APPLICANT,
                 String.join(caseData.getApplicant2().getFirstName(), caseData.getApplicant2().getLastName())))
                .code(UUID.randomUUID())
                .build();

            listElements.add(secondApplicant);
        }

        if (caseData.getBirthMother() != null && caseData.getBirthMother().getFirstName() != null
            && caseData.getBirthMother().getLastName() != null) {
            DynamicListElement birthMother = DynamicListElement.builder()
                .label(joinDynamicListLabel(DocumentSubmitter.BIRTH_MOTHER,
                 String.join(caseData.getBirthMother().getFirstName(), caseData.getBirthMother().getLastName())))
                .code(UUID.randomUUID())
                .build();

            listElements.add(birthMother);
        }
        if (caseData.getBirthFather() != null && caseData.getBirthFather().getFirstName() != null
            && caseData.getBirthFather().getLastName() != null) {
            DynamicListElement birthFather = DynamicListElement.builder()
                .label(joinDynamicListLabel(DocumentSubmitter.BIRTH_FATHER,
                String.join(caseData.getBirthFather().getFirstName(), caseData.getBirthFather().getLastName())))
                .code(UUID.randomUUID())
                .build();

            listElements.add(birthFather);
        }

        if (caseData.getOtherParent() != null && caseData.getOtherParent().getFirstName() != null
            && caseData.getOtherParent().getLastName() != null) {
            DynamicListElement personWithParentalResponsibility = DynamicListElement.builder()
                .label(joinDynamicListLabel(DocumentSubmitter.PERSON_WITH_PARENTAL_RESPONSIBILITY,
                String.join(caseData.getOtherParent().getFirstName(), caseData.getOtherParent().getLastName())))
                .code(UUID.randomUUID())
                .build();

            listElements.add(personWithParentalResponsibility);
        }
        var adoptionSeekFutherInfo = new AdoptionSeekFurtherInformation();
        adoptionSeekFutherInfo.setSeekFurtherInformationList(
            DynamicList.builder().listItems(listElements).value(DynamicListElement.EMPTY).build());
        caseData.setAdoptionSeekFurtherInformation(adoptionSeekFutherInfo);
        log.info("MidEvent Triggered");


        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .build();
    }

    /**
     * Method will return String after concatenation.
     *
     * @param enumLabel is typeof Document Submitter
     * @param detail is typeof label
     * @return will return String after join
     */
    private String joinDynamicListLabel(DocumentSubmitter enumLabel, String detail) {
        return String.join(BLANK_SPACE,enumLabel.getLabel(),STRING_COLON, detail);
    }

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(CaseDetails<CaseData, State> caseDataStateCaseDetails,
                                                                       CaseDetails<CaseData, State> caseDataStateCaseDetailsBefore) {
        var adoptionSeekFurtherInfo = new AdoptionSeekFurtherInformation();
        CaseData caseData = caseDataStateCaseDetails.getData();
        adoptionSeekFurtherInfo.setDate(caseData.getDate());
//        if(CollectionUtils.isEmpty(caseData.getAdoptionSeekFurtherInformationList())) {
//            caseData.setAdoptionSeekFurtherInformationList(addSeekInformationData(caseData,
//            caseData.getAdoptionSeekFurtherInformationList()));
//        }
        caseData.setSeekFurtherInfoList(addSeekInformationData(caseData,
                caseData.getSeekFurtherInfoList()));
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .build();
    }


    /**
     *
     * @param caseData
     * @param adoptionSeekFurtherList
     * @return
     */
    private List<ListValue<AdoptionSeekFurtherInformation>> addSeekInformationData(CaseData caseData,
            List<ListValue<AdoptionSeekFurtherInformation>> adoptionSeekFurtherList) {
        AdoptionSeekFurtherInformation manageSeekFurtherInformation = caseData.getAdoptionSeekFurtherInformation();
        manageSeekFurtherInformation.setDate(caseData.getDate());
        if(isEmpty(adoptionSeekFurtherList)) {
            List<ListValue<AdoptionSeekFurtherInformation>> listValues = new ArrayList<>();
            var listValue = ListValue.
                <AdoptionSeekFurtherInformation>builder().id("1").value(manageSeekFurtherInformation).build();
            listValues.add(listValue);
            return listValues;
        } else {
            AtomicInteger listValueIndex = new AtomicInteger(0);
            var listValue = ListValue
                .<AdoptionSeekFurtherInformation>builder()
                .value(manageSeekFurtherInformation)
                .build();
            adoptionSeekFurtherList.add(
                0,
                listValue
            );

            adoptionSeekFurtherList.forEach(adoptionDocumentListValue ->
                                                adoptionDocumentListValue.setId(
                                                    String.valueOf(listValueIndex.incrementAndGet())));
        }
        return adoptionSeekFurtherList;
    }
}
