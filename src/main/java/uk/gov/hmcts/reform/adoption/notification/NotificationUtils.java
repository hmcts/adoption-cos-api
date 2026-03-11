package uk.gov.hmcts.reform.adoption.notification;

import org.apache.commons.lang3.StringUtils;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;

import java.util.Optional;
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

    public static String mask(String email) {
        return Optional.ofNullable(email)
            .map(e -> {
                int at = e.indexOf('@');
                String user;
                String atAndDomain;
                if (at < 1 || at == e.length() - 1 || e.indexOf('@', at + 1) != -1) {
                    user = e; // invalid shape -> mask all
                    atAndDomain = "";
                } else {
                    user = e.substring(0, at);
                    atAndDomain = e.substring(at);
                }
                String maskedUser = user.length() <= 2
                    ? "*".repeat(user.length())
                    : user.charAt(0) + "*".repeat(user.length() - 2) + user.charAt(user.length() - 1);
                return maskedUser + atAndDomain;
            })
            .orElse("");
    }
}
