package uk.gov.hmcts.reform.adoption.adoptioncase.event;

//import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.Event;
import uk.gov.hmcts.ccd.sdk.type.TTL;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
import static uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.CaseworkerCaseNote.CASEWORKER_ADD_CASE_NOTE;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

class ManageCaseTtlTest extends EventTest {

    @InjectMocks
    private ManageCaseTtl manageCaseTtl;

    //@Test
    void shouldSetUpManageCaseTtlEvent() throws Exception {
        final ConfigBuilderImpl<CaseData, State, UserRole> configBuilder = createCaseDataConfigBuilder();

        manageCaseTtl.configure(configBuilder);

        assertThat(getEventsFrom(configBuilder).values())
            .extracting(Event::getId)
            .contains(CASEWORKER_ADD_CASE_NOTE);
    }

    private CaseDetails<CaseData, State> setupCaseWithTtl(YesOrNo suspendTtl, LocalDate overrideTtl) {
        final var details = new CaseDetails<CaseData, State>();
        final var data = caseData();
        TTL ttl = new TTL();
        ttl.setSystemTTL(LocalDate.of(2150,1,1));
        if (suspendTtl != null) {
            ttl.setSuspended(suspendTtl);
        }
        if (overrideTtl != null) {
            ttl.setOverrideTTL(overrideTtl);
        }
        data.setRetainAndDisposeTimeToLive(ttl);
        details.setData(data);
        details.setId(1L);

        return details;
    }

}
