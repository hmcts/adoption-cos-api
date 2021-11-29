package uk.gov.hmcts.reform.adoption.divorcecase;

import lombok.Builder;
import lombok.Getter;
import uk.gov.hmcts.reform.adoption.divorcecase.model.CaseData;
import uk.gov.hmcts.reform.adoption.divorcecase.model.State;

import java.util.List;

@Builder
@Getter
public class CaseInfo {

    private final CaseData caseData;
    private final State state;
    private final List<String> errors;
}
