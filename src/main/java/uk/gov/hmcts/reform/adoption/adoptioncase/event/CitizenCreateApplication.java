package uk.gov.hmcts.reform.adoption.adoptioncase.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.adoptioncase.search.CaseFieldsConstants;
import uk.gov.hmcts.reform.adoption.common.AddSystemUpdateRole;
import uk.gov.hmcts.reform.adoption.idam.IdamService;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataApi;
import uk.gov.hmcts.reform.ccd.client.model.SubmittedCallbackResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Draft;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole.CITIZEN;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.access.Permissions.CREATE_READ_UPDATE;

@Component
@Slf4j
public class CitizenCreateApplication implements CCDConfig<CaseData, State, UserRole> {

    public static final String CITIZEN_CREATE = "citizen-create-application";

    @Autowired
    private AddSystemUpdateRole addSystemUpdateRole;

    @Autowired
    private CoreCaseDataApi coreCaseDataApi;

    @Autowired
    private AuthTokenGenerator authTokenGenerator;

    @Autowired
    private IdamService idamService;

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        var defaultRoles = new ArrayList<UserRole>();
        defaultRoles.add(CITIZEN);

        var updatedRoles = addSystemUpdateRole.addIfConfiguredForEnvironment(defaultRoles);

        configBuilder
            .event(CITIZEN_CREATE)
            .initialState(Draft)
            .name("Create adoption draft case")
            .description("Apply for adoption")
            .aboutToSubmitCallback(this::aboutToSubmit)
            .submittedCallback(this::submitted)
            .grant(CREATE_READ_UPDATE, updatedRoles.toArray(UserRole[]::new))
            .retries(120, 120);
    }

    public SubmittedCallbackResponse submitted(CaseDetails<CaseData, State> details, CaseDetails<CaseData, State> beforeDetails) {
        final String authorisation = idamService.retrieveSystemUpdateUserDetails().getAuthToken();

        Map<String, Map<String, Map<String, Object>>> supplementaryData = new HashMap<>();
        supplementaryData.put("supplementary_data_updates",
                              Map.of("$set", Map.of("HMCTSServiceId", "ABA4")));

        coreCaseDataApi.submitSupplementaryData(authorisation, authTokenGenerator.generate(), String.valueOf(details.getId()),
                                                supplementaryData);

        return SubmittedCallbackResponse.builder().build();

    }

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(final CaseDetails<CaseData, State> details,
                                                                       final CaseDetails<CaseData, State> beforeDetails) {
        log.info("Citizen create adoption application about to submit callback invoked");

        CaseData data = details.getData();
        details.getData().setStatus(Draft);
        // Setting the default value so that its value is shown in Summary Tab and Amend Case details screen
        details.getData().setTypeOfAdoption(CaseFieldsConstants.TYPE_OF_ADOPTION);
        String temp = String.format("%016d", details.getId());
        data.setHyphenatedCaseRef(String.format(
            "%4s-%4s-%4s-%4s",
            temp.substring(0, 4),
            temp.substring(4, 8),
            temp.substring(8, 12),
            temp.substring(12, 16)
        ));
        setDssMetaData(data);

        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(data)
            .build();
    }

    private void setDssMetaData(CaseData data) {

        data.setDssQuestion1("Full Name");
        data.setDssQuestion2("DssQuestion2");
        data.setDssQuestion3("DssQuestion3");
        data.setDssAnswer1("case_data.childrenFirstName, case_data.childrenLastName");
        data.setDssAnswer2("case_data.childrenDateOfBirth");
        data.setDssAnswer3("case_data.otherAdoptionAgencyAddress.Country");
    }
}
