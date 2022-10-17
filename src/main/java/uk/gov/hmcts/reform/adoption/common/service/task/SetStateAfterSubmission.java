package uk.gov.hmcts.reform.adoption.common.service.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Application;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.task.CaseTask;

//TODO
// import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AwaitingDocuments;
// import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AwaitingHWFDecision;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.Submitted;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.AwaitingPayment;


@Component
@Slf4j
public class SetStateAfterSubmission implements CaseTask {

    @Override
    public CaseDetails<CaseData, State> apply(final CaseDetails<CaseData, State> caseDetails) {

        final CaseData caseData = caseDetails.getData();
        final Application application = caseData.getApplication();

        // if (application.isHelpWithFeesApplication()) {
        // caseDetails.setState(AwaitingHWFDecision);
        // } else
        if (!application.hasBeenPaidFor()) {
            caseDetails.setState(AwaitingPayment);
        // } else if (application.hasAwaitingApplicant1Documents()) {
        //   caseDetails.setState(AwaitingDocuments);
        } else {
            caseDetails.setState(Submitted);
        }

        log.info("State set to {}, CaseID {}", caseDetails.getState(), caseDetails.getId());
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JSR310Module());
            mapper.registerModule(new JavaTimeModule());
            //mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            //mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
            log.info("CaseData<<<<<<<<<<<<<<>>>>>>>>>>> {}", mapper.writeValueAsString(caseDetails));
        } catch (Exception e) {
            log.error("Exception");
        }


        return caseDetails;
    }
}
