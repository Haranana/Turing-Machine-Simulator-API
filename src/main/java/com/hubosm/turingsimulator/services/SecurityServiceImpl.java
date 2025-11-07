package com.hubosm.turingsimulator.services;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SecurityServiceImpl implements SecurityService{


    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String encode(String toEncode){
        return passwordEncoder.encode(toEncode);
    }


}
