package com.hubosm.turingsimulator.controllers;

import com.hubosm.turingsimulator.dtos.*;
import com.hubosm.turingsimulator.services.TuringMachineService;
import com.hubosm.turingsimulator.services.TuringMachineServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final TuringMachineService turingMachineService;

    @PostMapping
    public ResponseEntity<TuringMachineReturnDto> add(@Valid @RequestBody TuringMachineCreateDto dto) throws Exception {
        TuringMachineReturnDto returnDto = turingMachineService.addTuringMachine(dto);
        return ResponseEntity.status(HttpStatus.CREATED).
                header("Location", "/api/tm/" + returnDto.getId()).
                body(returnDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TuringMachineReturnDto> edit(@PathVariable Long id, @Valid @RequestBody TuringMachineEditDto dto) throws Exception{
        dto.setId(id);
        TuringMachineReturnDto returnDto = turingMachineService.editTuringMachine(dto);
        return ResponseEntity.ok(returnDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws Exception{
        turingMachineService.deleteTuringMachine(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TuringMachineReturnDto> getOne(@PathVariable Long id) throws Exception {
        TuringMachineReturnDto returnDto = turingMachineService.getTuringMachine(id);
        return ResponseEntity.ok(returnDto);
    }

    @GetMapping
    public ResponseEntity<Page<TuringMachineReturnDto>> getByAuthor(
            @RequestParam Long authorId,
            @PageableDefault(size = 50) Pageable pageable) {
        Page<TuringMachineReturnDto> returnDtoPage = turingMachineService.getTuringMachinesByUserId(authorId, pageable);
        return ResponseEntity.ok(returnDtoPage);
    }

}
