package com.kamyla.email_service.infra.ses;

import com.kamyla.email_service.adapters.EmailSenderGateway;
import com.kamyla.email_service.core.exceptions.EmailServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.*;

@Qualifier("ses")
@Service
public class SesEmailSender implements EmailSenderGateway {

    private final SesV2Client sesV2Client;

    @Value("${mail.sender}")
    private String senderEmail;

    @Autowired
    public SesEmailSender(SesV2Client sesV2Client) {
        this.sesV2Client = sesV2Client;
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        SendEmailRequest request = SendEmailRequest.builder()
                .destination(Destination
                        .builder()
                        .toAddresses(to)
                        .build())
                .fromEmailAddress(senderEmail)
                .content(EmailContent.builder()
                        .simple(Message.builder()
                                .subject(Content.builder().data(subject).build())
                                .body(Body.builder()
                                        .text(Content.builder().data(body).build())
                                        .build())
                                .build())
                        .build())
                .build();

        try {
            this.sesV2Client.sendEmail(request);
        } catch (SesV2Exception exception) {
            throw new EmailServiceException("Failure while sending email", exception);
        }
    }
}
