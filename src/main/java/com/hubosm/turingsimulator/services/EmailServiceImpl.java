package com.hubosm.turingsimulator.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender mailSender;

    @Value("${app.url}")
    private String appUrl;

    @Override
    public void sendActivationMail(String userMail, String activationToken, String expirationDate){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(userMail);
        msg.setSubject("Turing Machine Account Activation");
        msg.setText("To activate your account please enter URL: " + appUrl+"activate?token="+  activationToken + "\nActivation link expires on: " + expirationDate);
        mailSender.send(msg);
    }
}
