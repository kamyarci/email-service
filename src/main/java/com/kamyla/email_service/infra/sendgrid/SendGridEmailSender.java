package com.kamyla.email_service.infra.sendgrid;

import com.kamyla.email_service.adapters.EmailSenderGateway;
import com.kamyla.email_service.core.exceptions.EmailServiceException;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Qualifier("sendgrid")
@Slf4j
@Service
public class SendGridEmailSender implements EmailSenderGateway {

    private final SendGrid sendGridClient;

    @Value("${mail.sender}")
    private String senderEmail;

    @Autowired
    public SendGridEmailSender(SendGrid sendGridClient) {
        this.sendGridClient = sendGridClient;
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        Email fromEmail = new Email(senderEmail);
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(fromEmail, subject, toEmail, content);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGridClient.api(request);

            if (response.getStatusCode() >= 400) {
                throw new EmailServiceException("SendGrid error: " + response.getStatusCode());
            }

            log.info("Email sent successfully to {}", to);
        } catch (IOException e) {
            throw new EmailServiceException("Failure while sending email", e);
        }

    }
}
