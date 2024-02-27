package com.dominicjmarshall.springbootday1.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
    
    @Value("${spring.mail.host}")
    private String mailHost;
    @Value("${spring.mail.port}")
    private String mailPort;
    @Value("${spring.mail.username}")
    private String mailUsername;
    @Value("${spring.mail.password}")
    private String mailPassword;

    @Value("${spring.mail.properties.smtp.auth}")
    private String smtpAuth;
    @Value("${spring.mail.properties.smtp.starttls.enable}")
    private String startTlsEnable;
    @Value("${spring.mail.smtp.ssl.trust}")
    private String sslTrust;
    @Value("${mail.transport.protocol}")
    private String transportProtocol;

    @Bean
    public JavaMailSender getJavaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setPort(Integer.parseInt(mailPort));
        
        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.debug", "true");
        props.put("mail.smtp.auth", smtpAuth);
        props.put("mail.smtp.starttls.enable", startTlsEnable);
        props.put("mail.smtp.ssl.trust", sslTrust);
        props.put("mail.transport.protocol", transportProtocol);
        
        return mailSender;
    }
}
