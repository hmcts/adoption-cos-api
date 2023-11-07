package uk.gov.hmcts.reform.adoption.service.event;

import lombok.Builder;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;

@Builder
public record ApplicationSubmitNotificationEvent(CaseDetails<CaseData, State> caseData) { }
