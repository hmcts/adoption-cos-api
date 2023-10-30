package uk.gov.hmcts.reform.adoption.notification;


import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.LOCAL_COURT_EMAIL_SENDGRID_ENDPOINT;

@Slf4j
public class TemplatePoc {

    @Value("${send-grid.api-key}")
    private String apiKey;


    public void sendEmailUsingTemplate() {
        Mail mail = new Mail();
        Email toEmail = new Email("send-grid-to-email-to-go-here");
        Email fromEmail = new Email("send-grid-from-email-to-go-here");
        mail.setFrom(fromEmail);
        Personalization personalization = new Personalization();
        personalization.addTo(toEmail);
        personalization.addCustomArg("localCourtName","Test Local court name");
        personalization.addDynamicTemplateData("localCourtName","Test Local court name");
        personalization.addDynamicTemplateData("hyphenatedCaseRef","1234-1234-1234-1234");
        personalization.addDynamicTemplateData("childFullName","ChildFirstName ChildLastName");
        personalization.addDynamicTemplateData("dateSubmitted","15th October 2023");
        personalization.addDynamicTemplateData("laPortalURL","https://www.google.com");
        mail.addPersonalization(personalization);
        mail.setTemplateId("send-grid-template-id-to-go-here");
        SendGrid sg = new SendGrid("send-Grid-Secret-will-go-here");
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint(LOCAL_COURT_EMAIL_SENDGRID_ENDPOINT);
            request.setBody(mail.build());
            sg.api(request);
            log.info("Notification email to Local Court sent successfully");
        } catch (IOException ex) {
            log.error("Notification email to Local Court failed {}",ex.getMessage());
        }
    }
}
