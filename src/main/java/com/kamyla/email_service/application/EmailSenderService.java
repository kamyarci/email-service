package com.kamyla.email_service.application;

import com.kamyla.email_service.adapters.EmailSenderGateway;
import com.kamyla.email_service.core.EmailSenderUseCase;
import com.kamyla.email_service.core.exceptions.EmailServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailSenderService implements EmailSenderUseCase {

    private final EmailSenderGateway primaryGateway;
    private final EmailSenderGateway fallbackGateway;

    @Autowired
    public EmailSenderService(@Qualifier("ses") EmailSenderGateway primaryGateway,
                              @Qualifier("sendgrid") EmailSenderGateway fallbackGateway) {
        this.primaryGateway = primaryGateway;
        this.fallbackGateway = fallbackGateway;
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        try {
            this.primaryGateway.sendEmail(to, subject, body);
        } catch (EmailServiceException e) {
            log.warn("Primary email gateway failed, switching to fallback. Error: {}", e.getMessage());
            this.fallbackGateway.sendEmail(to, subject, body);
        }
    }
}
