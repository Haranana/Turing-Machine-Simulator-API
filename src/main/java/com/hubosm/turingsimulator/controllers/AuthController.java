package com.hubosm.turingsimulator.controllers;

import com.hubosm.turingsimulator.dtos.AuthResponseDto;
import com.hubosm.turingsimulator.dtos.LoginRequestDto;
import com.hubosm.turingsimulator.dtos.RegisterRequestDto;
import com.hubosm.turingsimulator.dtos.UserCreateDto;
import com.hubosm.turingsimulator.services.AuthServiceImpl;
import com.hubosm.turingsimulator.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthServiceImpl authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto dto){
       return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserCreateDto dto) throws Exception {
        userService.createUser(dto);
        return ResponseEntity.ok().build();
    }
}
