package uk.gov.hmcts.reform.adoption.systemupdate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;

@Component
public class CaseDetailsConverter {

    @Autowired
    private ObjectMapper objectMapper;

    public uk.gov.hmcts.ccd.sdk.api.CaseDetails<CaseData, State> convertToCaseDetailsFromReformModel(final CaseDetails caseDetails) {
        return objectMapper.convertValue(caseDetails, new TypeReference<>() {
        });
    }

}
