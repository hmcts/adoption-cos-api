package uk.gov.hmcts.reform.adoption.adoptioncase.common;

import org.apache.commons.lang3.StringUtils;
import uk.gov.hmcts.reform.idam.client.models.User;

public final class CaseEventCommonMethods {
    private CaseEventCommonMethods() {

    }

    public static String buildMessageHistory(User caseworkerUser, String messageText, String messageHistory) {
        String newMessage = String.format(
            "%s %s %s",
            caseworkerUser.getUserDetails().getEmail(),
            " - ",
            messageText);
        if (StringUtils.isEmpty(messageHistory)) {
            return newMessage;
        }
        return String.join("\n \n", newMessage, messageHistory);
    }
}
