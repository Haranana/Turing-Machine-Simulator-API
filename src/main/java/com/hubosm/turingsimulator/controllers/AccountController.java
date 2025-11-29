package com.hubosm.turingsimulator.controllers;

import com.hubosm.turingsimulator.dtos.UserReturnDto;
import com.hubosm.turingsimulator.entities.User;
import com.hubosm.turingsimulator.mappers.UserMapper;
import com.hubosm.turingsimulator.services.UserService;
import com.hubosm.turingsimulator.services.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/account")
@RestController
@RequiredArgsConstructor
public class AccountController {
    private final UserMapper userMapper;
    private final UserServiceImpl userService;

    @GetMapping()
    public ResponseEntity<UserReturnDto> getUserProfile(@AuthenticationPrincipal User principal){
        return ResponseEntity.ok(userMapper.EntityToReturnDto(principal));
    }

    @PostMapping("/activate")
    public ResponseEntity<Void> activate(@RequestParam("token") String token) throws Exception {
        userService.activateUserAccount(token);
        return ResponseEntity.ok().build();
    }

}
