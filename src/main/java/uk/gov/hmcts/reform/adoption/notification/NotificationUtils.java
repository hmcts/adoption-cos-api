package uk.gov.hmcts.reform.adoption.notification;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class NotificationUtils {

    public Set<String> collectUniqueLocalAuthorityEmails(CaseData caseData) {
        return java.util.stream.Stream.of(
                caseData.getChildSocialWorker() != null ? caseData.getChildSocialWorker().getLocalAuthorityEmail() : "",
                caseData.getChildSocialWorker() != null ? caseData.getChildSocialWorker().getSocialWorkerEmail() : "",
                caseData.getApplicantSocialWorker() != null ? caseData.getApplicantSocialWorker().getLocalAuthorityEmail() : "",
                caseData.getApplicantSocialWorker() != null ? caseData.getApplicantSocialWorker().getSocialWorkerEmail() : ""
            )
            .filter(StringUtils::isNotBlank)
            .map(String::toLowerCase)
            .collect(java.util.stream.Collectors.toSet());
    }
}
