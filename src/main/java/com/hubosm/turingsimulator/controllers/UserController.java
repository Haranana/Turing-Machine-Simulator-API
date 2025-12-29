package com.hubosm.turingsimulator.controllers;

import com.hubosm.turingsimulator.dtos.*;
import com.hubosm.turingsimulator.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @DeleteMapping
    public ResponseEntity<Void> delete( @RequestBody Long id) throws Exception{
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<UserReturnDto> get(@RequestBody Long id) throws Exception{
        UserReturnDto dto = userService.getUser(id);
        return ResponseEntity.ok(dto);
    }




}
