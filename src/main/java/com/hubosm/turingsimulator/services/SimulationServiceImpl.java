package com.hubosm.turingsimulator.services;

import com.hubosm.turingsimulator.domain.*;
import com.hubosm.turingsimulator.domain.Nondeterministic.NdTmSimulator;
import com.hubosm.turingsimulator.dtos.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class SimulationServiceImpl implements SimulationService {

    public SimulationReturnDto runSimulation(SimulationCreateDto dto){
        if (dto.getInput().size() != dto.getTapesAmount())
            throw new IllegalArgumentException("input size must equal tapesAmount");

        TuringMachineSimulator tm = new TuringMachineSimulator(
                dto.getInitialState(),
                dto.getAcceptState(),
                dto.getRejectState(),
                dto.getProgram(),
                dto.getSep1(),
                dto.getSep2(),
                dto.getBlank(),
                dto.getTapesAmount()
        );
        return tm.runSimulation(
                dto.getInput());
    }

    public NonDetSimulationDto runNdSimulation(SimulationCreateDto dto){
        if (dto.getInput().size() != dto.getTapesAmount())
            throw new IllegalArgumentException("input size must equal tapesAmount");

        NdTmSimulator tm = new NdTmSimulator(
                dto.getInitialState(),
                dto.getAcceptState(),
                dto.getRejectState(),
                dto.getProgram(),
                dto.getSep1(),
                dto.getSep2(),
                dto.getBlank(),
                dto.getTapesAmount()
        );
        return tm.createSimulation(
                dto.getInput());
    }
}
