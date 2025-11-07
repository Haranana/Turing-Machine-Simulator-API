package com.hubosm.turingsimulator.controllers;

import com.hubosm.turingsimulator.dtos.UserReturnDto;
import com.hubosm.turingsimulator.entities.User;
import com.hubosm.turingsimulator.mappers.UserMapper;
import com.hubosm.turingsimulator.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/account")
@RestController
@RequiredArgsConstructor
public class AccountController {
    private final UserMapper userMapper;

    @GetMapping()
    public ResponseEntity<UserReturnDto> getUserProfile(@AuthenticationPrincipal User principal){
        return ResponseEntity.ok(userMapper.EntityToReturnDto(principal));
    }

}
