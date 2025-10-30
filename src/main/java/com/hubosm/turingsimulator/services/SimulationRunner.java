package com.hubosm.turingsimulator.services;

import com.hubosm.turingsimulator.domain.FullSimulationStep;
import com.hubosm.turingsimulator.dtos.CreateTuringMachineDto;
import com.hubosm.turingsimulator.dtos.SimulationStepDto;

import java.util.function.BiConsumer;

public interface SimulationRunner {
    void run(CreateTuringMachineDto dto, BiConsumer<Integer, SimulationStepDto> onStepUpdate);

}
