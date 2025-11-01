package com.hubosm.turingsimulator.services;

import com.hubosm.turingsimulator.domain.TuringMachine;
import com.hubosm.turingsimulator.dtos.CreateTuringMachineDto;
import com.hubosm.turingsimulator.dtos.SimulationStepDto;
import com.hubosm.turingsimulator.mappers.TuringMachineMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.BiConsumer;
/*
@Service
@RequiredArgsConstructor
public class SimulationRunnerImpl implements SimulationRunner{

    private final TuringMachineMapper turingMachineMapper;

    @Override
    public void run(CreateTuringMachineDto dto, BiConsumer<Integer, SimulationStepDto> onStepUpdate) {
        turingMachineMapper.createDtoToDomain(dto).run(dto.getInput(), onStepUpdate);
    }
}*/
