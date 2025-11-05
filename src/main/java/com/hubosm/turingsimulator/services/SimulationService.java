package com.hubosm.turingsimulator.services;

import com.hubosm.turingsimulator.dtos.*;

import java.util.List;
import java.util.UUID;

public interface SimulationService {

    SimulationReturnDto runSimulation(SimulationCreateDto dto);

}

