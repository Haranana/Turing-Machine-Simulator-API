package com.hubosm.turingsimulator.services;

import com.hubosm.turingsimulator.dtos.*;

public interface SimulationService {
    SimulationReturnDto runSimulation(SimulationCreateDto dto);
}

