package com.kamyla.email_service.infra.sendgrid;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendGridConfig {

    @Value("${sendgrid.apiKey}")
    private String apiKey;

    @Bean
    public SendGrid sendGridClient() {
        return new SendGrid(apiKey);
    }
}
