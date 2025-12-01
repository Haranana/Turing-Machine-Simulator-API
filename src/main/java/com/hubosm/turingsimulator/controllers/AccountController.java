package com.hubosm.turingsimulator.controllers;

import com.hubosm.turingsimulator.dtos.UserChangePasswordDto;
import com.hubosm.turingsimulator.dtos.UserPasswordChangeRequestDto;
import com.hubosm.turingsimulator.dtos.UserReturnDto;
import com.hubosm.turingsimulator.entities.User;
import com.hubosm.turingsimulator.mappers.UserMapper;
import com.hubosm.turingsimulator.services.UserService;
import com.hubosm.turingsimulator.services.UserServiceImpl;
import com.sun.security.auth.UserPrincipal;
import jakarta.validation.Valid;
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

    @PostMapping("/password/token")
    public ResponseEntity<Void> generatePasswordChangeToken(@Valid @RequestBody UserPasswordChangeRequestDto dto) throws Exception {
        userService.addChangePasswordToken(dto.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/change")
    public ResponseEntity<Void> generatePasswordChangeToken(@Valid @RequestBody UserChangePasswordDto dto,
                                                            @RequestParam("token") String token) throws Exception {
        userService.changePassword(dto, token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete/token")
    public ResponseEntity<Void> generateDeleteAccountToken(@AuthenticationPrincipal User principal) throws Exception {
        userService.addDeleteAccountToken(principal.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete/confirm")
    public ResponseEntity<Void> deleteAccount(@RequestParam("token") String token) throws Exception {
        userService.deleteAccount(token);
        return ResponseEntity.ok().build();
    }
}
