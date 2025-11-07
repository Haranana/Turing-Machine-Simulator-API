package com.hubosm.turingsimulator.services;

import com.hubosm.turingsimulator.dtos.AuthResponseDto;
import com.hubosm.turingsimulator.dtos.LoginRequestDto;
import com.hubosm.turingsimulator.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl{

    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final JwtServiceImpl jwtService;

    public AuthResponseDto login(LoginRequestDto req) {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(req.email(), req.password());
        authManager.authenticate(token);
        var user = userRepository.findByEmail(req.email()).orElseThrow();
        var jwtStr = jwtService.generateAccessToken(user);
        return new AuthResponseDto(jwtStr, "Bearer", jwtService.ttlSeconds());
    }
}
