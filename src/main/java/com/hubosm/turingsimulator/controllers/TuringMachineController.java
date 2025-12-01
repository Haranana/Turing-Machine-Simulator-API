package com.hubosm.turingsimulator.controllers;

import com.hubosm.turingsimulator.dtos.*;
import com.hubosm.turingsimulator.entities.User;
import com.hubosm.turingsimulator.services.TuringMachineService;
import com.hubosm.turingsimulator.services.TuringMachineServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tm")
@RequiredArgsConstructor
public class TuringMachineController {
    private final TuringMachineService turingMachineService;

    @PostMapping
    public ResponseEntity<TuringMachineReturnDto> add(@Valid @RequestBody TuringMachineCreateDto dto,
    @AuthenticationPrincipal User principal) throws Exception {
        TuringMachineReturnDto returnDto = turingMachineService.addTuringMachine(dto, principal.getId());
        return ResponseEntity.status(HttpStatus.CREATED).
                header("Location", "/api/tm/" + returnDto.getId()).
                body(returnDto);
    }

    @PutMapping
    public ResponseEntity<TuringMachineReturnDto> edit(@Valid @RequestBody TuringMachineEditDto dto,
                                                       @AuthenticationPrincipal User principal) throws Exception{
        TuringMachineReturnDto returnDto = turingMachineService.editTuringMachine(dto, principal.getId());
        return ResponseEntity.ok(returnDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal User principal,
                                       @PathVariable Long id) throws Exception{
        System.out.println(principal + " " + id );
        turingMachineService.deleteTuringMachine(id , principal.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TuringMachineReturnDto> getOne(@PathVariable Long id) throws Exception {
        TuringMachineReturnDto returnDto = turingMachineService.getTuringMachine(id);
        return ResponseEntity.ok(returnDto);
    }

    @GetMapping("/exists/{name}")
    public ResponseEntity<Optional<TuringMachineReturnDto>> exists(@AuthenticationPrincipal User principal,
                                          @PathVariable String tmName) throws Exception{
        Optional<TuringMachineReturnDto> result = turingMachineService.existsByNameAndAuthor(tmName, principal.getId());
        if(result.isPresent()){
            return ResponseEntity.ok(result);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @GetMapping
    public ResponseEntity<Page<TuringMachineReturnDto>> getByAuthor(
            @AuthenticationPrincipal User principal,
            @PageableDefault(size = 50) Pageable pageable) {
        //System.out.println(principal +  " : " + pageable);
        Page<TuringMachineReturnDto> returnDtoPage = turingMachineService.getTuringMachinesByUserId(principal.getId(), pageable);

        //System.out.println(returnDtoPage.getContent().get(0));
        return ResponseEntity.ok(returnDtoPage);
    }

    @PostMapping("/visibility/{id}")
    public ResponseEntity<Void> toggleTmVisibility(@AuthenticationPrincipal User principal, @PathVariable("id") Long id) throws Exception{
        turingMachineService.toggleTmVisibility(id, principal.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/public/{shareCode}")
    public ResponseEntity<TuringMachineReturnDto> getTmByShareCode(@PathVariable("shareCode") String shareCode) throws Exception{
        TuringMachineReturnDto returnDto = turingMachineService.getTuringMachineByShareCode(shareCode);
        return ResponseEntity.ok(returnDto);
    }

}
