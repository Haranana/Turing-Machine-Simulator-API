package com.hubosm.turingsimulator.services;

import com.hubosm.turingsimulator.dtos.AuthResponseDto;
import com.hubosm.turingsimulator.dtos.LoginRequestDto;
import com.hubosm.turingsimulator.exceptions.AccessDeniedException;
import com.hubosm.turingsimulator.exceptions.AccountNotActiveException;
import com.hubosm.turingsimulator.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
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
        try {
            authManager.authenticate(token);
        } catch (DisabledException ex) {
            throw new AccountNotActiveException("User account is not active");
        } catch (BadCredentialsException ex){
            throw new AccessDeniedException("Bad Credentials");
        }

        var user = userRepository.findByEmail(req.email()).orElseThrow(() -> new AccessDeniedException("Bad Credentials"));
        if(!user.isEnabled()) throw new AccountNotActiveException("User account is not active");
        var jwtStr = jwtService.generateAccessToken(user);
        return new AuthResponseDto(jwtStr, "Bearer", jwtService.ttlSeconds());
    }
}
