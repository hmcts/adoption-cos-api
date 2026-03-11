package uk.gov.hmcts.reform.adoption.notification;

import org.apache.commons.lang3.StringUtils;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class NotificationUtils {

    private NotificationUtils() {
    }

    public static Set<String> collectUniqueLocalAuthorityEmails(CaseData caseData) {
        return Stream.of(
                caseData.getChildSocialWorker() != null ? caseData.getChildSocialWorker().getLocalAuthorityEmail() : "",
                caseData.getChildSocialWorker() != null ? caseData.getChildSocialWorker().getSocialWorkerEmail() : "",
                caseData.getApplicantSocialWorker() != null ? caseData.getApplicantSocialWorker().getLocalAuthorityEmail() : "",
                caseData.getApplicantSocialWorker() != null ? caseData.getApplicantSocialWorker().getSocialWorkerEmail() : ""
            )
            .filter(StringUtils::isNotBlank)
            .map(String::toLowerCase)
            .collect(toSet());
    }
}
