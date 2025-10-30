package com.hubosm.turingsimulator.services;

import com.hubosm.turingsimulator.domain.FullSimulationStep;
import com.hubosm.turingsimulator.dtos.*;

import java.util.List;
import java.util.UUID;

public interface SimulationService {
    UUID queueSimulation(CreateTuringMachineDto dto);
    SimulationStatusDto getStatus(UUID jobId);
    List<SimulationStepDto> getSteps(UUID jobId, int offset, int limit);
    CreatedSimulationDto runSimulation(CreateSimulationDto dto);
}
