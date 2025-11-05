package com.hubosm.turingsimulator.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SecurityServiceImpl implements SecurityService{
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String encode(String toEncode){
        return passwordEncoder.encode(toEncode);
    }
}
