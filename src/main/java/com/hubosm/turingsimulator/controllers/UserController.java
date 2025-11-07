package com.hubosm.turingsimulator.controllers;

import com.hubosm.turingsimulator.dtos.*;
import com.hubosm.turingsimulator.services.TuringMachineServiceImpl;
import com.hubosm.turingsimulator.services.UserService;
import com.hubosm.turingsimulator.services.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /*
    @PostMapping
    public ResponseEntity<Void> add(@Valid @RequestBody UserCreateDto dto) throws Exception {
        userService.createUser(dto);
        return ResponseEntity.ok().build();
    }*/

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
