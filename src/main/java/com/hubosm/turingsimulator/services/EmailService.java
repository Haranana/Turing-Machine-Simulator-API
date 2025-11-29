package com.hubosm.turingsimulator.services;

public interface EmailService {
    void sendActivationMail(String userMail, String activationToken, String expirationDate);
}
