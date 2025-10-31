package com.hubosm.turingsimulator.controllers;


import com.hubosm.turingsimulator.dtos.*;
import com.hubosm.turingsimulator.services.SimulationServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/simulations")
@RequiredArgsConstructor
public class SimulationRestController {

    private final SimulationServiceImpl simulationService;


    @PostMapping
    public ResponseEntity<CreatedSimulationDto> simulate(@Valid @RequestBody CreateSimulationDto dto){
        CreatedSimulationDto outputDto = simulationService.runSimulation(dto);
        return ResponseEntity.accepted().body(outputDto);
    }

    /*
    @PostMapping
    public ResponseEntity<CreateSimulationDto> simulate(@Valid @RequestBody CreateSimulationDto dto){
        return ResponseEntity.accepted().body(dto);
    }*/

    /*
    @PostMapping
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<SimulationCreatedDto> simulate(@Valid @RequestBody CreateTuringMachineDto dto){
        //UUID jobID = simulationService.queueSimulation(dto);
        //return ResponseEntity.accepted().location(URI.create("/api/simulations/"+ jobID.toString())).body(new SimulationCreatedDto(jobID));
        return ResponseEntity.accepted().body(new SimulationCreatedDto(UUID.fromString("1")));
    }*/

    @GetMapping("/{id}")
    public ResponseEntity<SimulationStatusDto> getStatus(@PathVariable UUID id){
        return ResponseEntity.ok(simulationService.getStatus(id));
    }

    @GetMapping("/{id}/steps")
    public ResponseEntity<List<SimulationStepDto>> getSimulationSteps(@PathVariable UUID id,
                                                                      @RequestParam int offset,
                                                                      @RequestParam int limit){
        return ResponseEntity.ok(simulationService.getSteps(id,offset,limit));
    }
}
