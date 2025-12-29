package com.hubosm.turingsimulator.services;

import com.hubosm.turingsimulator.domain.TmSimulator;
import com.hubosm.turingsimulator.dtos.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimulationServiceImpl implements SimulationService {

    public SimulationReturnDto runSimulation(SimulationCreateDto dto){
        if (dto.getInput().size() != dto.getTapesAmount())
            throw new IllegalArgumentException("input size must equal tapesAmount");

        TmSimulator tm = new TmSimulator(
                dto.getInitialState(),
                dto.getAcceptState(),
                dto.getRejectState(),
                dto.getProgram(),
                dto.getSep1(),
                dto.getSep2(),
                dto.getBlank(),
                dto.getTapesAmount(),
                dto.isRejectOnNonAccept()
        );
        return tm.createSimulation(
                dto.getInput());
    }
}
