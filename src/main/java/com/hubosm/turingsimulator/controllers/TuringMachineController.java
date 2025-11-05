package com.hubosm.turingsimulator.controllers;

import com.hubosm.turingsimulator.dtos.*;
import com.hubosm.turingsimulator.services.TuringMachineServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tm")
@RequiredArgsConstructor
public class TuringMachineController {
    private final TuringMachineServiceImpl turingMachineService;

    @PostMapping
    public ResponseEntity<Void> add(@Valid @RequestBody TuringMachineCreateDto dto) throws Exception {
        turingMachineService.addTuringMachine(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> edit(@Valid @RequestBody TuringMachineEditDto dto) throws Exception{
        turingMachineService.editTuringMachine(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete( @RequestBody Long id) throws Exception{
        turingMachineService.deleteTuringMachine(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TuringMachineReturnDto> getOne(@PathVariable Long id) throws Exception {
        var dto = turingMachineService.getTuringMachine(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<TuringMachineReturnDto>> getByAuthor(
            @RequestParam Long authorId,
            @PageableDefault(size = 50) Pageable pageable) {
        List<TuringMachineReturnDto> dtos = turingMachineService.getTuringMachinesByUserId(authorId, pageable);
        return ResponseEntity.ok(dtos);
    }

}
