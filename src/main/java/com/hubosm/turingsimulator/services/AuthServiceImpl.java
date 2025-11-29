package com.hubosm.turingsimulator.services;

import com.hubosm.turingsimulator.dtos.AuthResponseDto;
import com.hubosm.turingsimulator.dtos.LoginRequestDto;
import com.hubosm.turingsimulator.exceptions.AccountNotActiveException;
import com.hubosm.turingsimulator.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl{

    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final JwtServiceImpl jwtService;
    private final SecureRandom secureRandom = new SecureRandom();
    private final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();

    public AuthResponseDto login(LoginRequestDto req) {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(req.email(), req.password());
        authManager.authenticate(token);
        var user = userRepository.findByEmail(req.email()).orElseThrow();
        if(!user.isEnabled()) throw new AccountNotActiveException("User account is not active");
        var jwtStr = jwtService.generateAccessToken(user);
        return new AuthResponseDto(jwtStr, "Bearer", jwtService.ttlSeconds());
    }

    public String generateActivationToken(){
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
