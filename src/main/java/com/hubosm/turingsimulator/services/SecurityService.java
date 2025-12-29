package com.hubosm.turingsimulator.services;

import org.springframework.stereotype.Service;

@Service
public interface SecurityService {

    String encode(String toEncode);
    String generateSecureToken();
}
