package com.hubosm.turingsimulator.services;

import com.hubosm.turingsimulator.dtos.*;

public interface SimulationService {
    NonDetSimulationDto runSimulation(SimulationCreateDto dto);
}

