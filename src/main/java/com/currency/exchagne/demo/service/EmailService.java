package com.currency.exchagne.demo.service;

import com.currency.exchagne.demo.model.ExchangeRate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(String toEmail,String body,String subject){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("tariktarelka@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);

        log.info("Mail Sent successfully!");
    }
}
