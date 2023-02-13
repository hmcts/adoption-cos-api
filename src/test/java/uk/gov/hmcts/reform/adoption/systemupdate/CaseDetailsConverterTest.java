package uk.gov.hmcts.reform.adoption.systemupdate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.adoption.adoptioncase.Adoption.CASE_TYPE;
import static uk.gov.hmcts.reform.adoption.adoptioncase.Adoption.JURISDICTION;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Submitted;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.LOCAL_DATE;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.LOCAL_DATE_TIME;
import static uk.gov.hmcts.reform.ccd.client.model.Classification.PUBLIC;

@ExtendWith(MockitoExtension.class)
class CaseDetailsConverterTest {

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @InjectMocks
    private CaseDetailsConverter caseDetailsConverter;

    @Test
    void shouldConvertToCaseDetailsFromReformModelCaseDetails() {

        final long id = 456L;
        final int lockedBy = 5;
        final int securityLevel = 5;
        final String callbackResponseStatus = "Status";

        final CaseDetails reformCaseDetails = CaseDetails.builder()
            .id(456L)
            .jurisdiction(JURISDICTION)
            .caseTypeId(CASE_TYPE)
            .createdDate(LOCAL_DATE_TIME)
            .lastModified(LOCAL_DATE_TIME)
            .state(Submitted.name())
            .lockedBy(lockedBy)
            .securityLevel(securityLevel)
            .data(Map.of("createdDate", LOCAL_DATE))
            .securityClassification(PUBLIC)
            .callbackResponseStatus(callbackResponseStatus)
            .build();

        final uk.gov.hmcts.ccd.sdk.api.CaseDetails<CaseData, State> caseDetails =
            caseDetailsConverter.convertToCaseDetailsFromReformModel(reformCaseDetails);

        assertThat(caseDetails.getId()).isEqualTo(id);
        assertThat(caseDetails.getJurisdiction()).isEqualTo(JURISDICTION);
        assertThat(caseDetails.getCaseTypeId()).isEqualTo(CASE_TYPE);
        assertThat(caseDetails.getCreatedDate()).isEqualTo(LOCAL_DATE_TIME);
        assertThat(caseDetails.getLastModified()).isEqualTo(LOCAL_DATE_TIME);
        assertThat(caseDetails.getState()).isEqualTo(Submitted);
        assertThat(caseDetails.getLockedBy()).isEqualTo(lockedBy);
        assertThat(caseDetails.getSecurityLevel()).isEqualTo(securityLevel);
        assertThat(caseDetails.getData().getApplication().getCreatedDate()).isEqualTo(LOCAL_DATE);
        assertThat(caseDetails.getSecurityClassification()).isEqualTo(PUBLIC);
        assertThat(caseDetails.getCallbackResponseStatus()).isEqualTo(callbackResponseStatus);
    }

    private Map<Object, Object> expectedData(final CaseData caseData) {
        return objectMapper.convertValue(caseData, new TypeReference<>() {
        });
    }
}
