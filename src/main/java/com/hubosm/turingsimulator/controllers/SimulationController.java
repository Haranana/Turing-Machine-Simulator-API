package com.hubosm.turingsimulator.controllers;


import com.hubosm.turingsimulator.dtos.*;
import com.hubosm.turingsimulator.services.SimulationServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/simulations")
@RequiredArgsConstructor
public class SimulationController {

    private final SimulationServiceImpl simulationService;


    @PostMapping
    public ResponseEntity<SimulationReturnDto> add(@Valid @RequestBody SimulationCreateDto dto){
        System.out.println("got dto in controller");
        SimulationReturnDto outputDto = simulationService.runSimulation(dto);
        return ResponseEntity.accepted().body(outputDto);
    }
}
