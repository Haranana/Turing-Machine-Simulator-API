package com.hubosm.turingsimulator.services;

import com.hubosm.turingsimulator.domain.*;
import com.hubosm.turingsimulator.dtos.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimulationServiceImpl implements SimulationService{


    public SimulationReturnDto runSimulation(SimulationCreateDto dto){
        TuringMachineSimulator tm = new TuringMachineSimulator(dto.getInitialState(), dto.getAcceptState(), dto.getRejectState(), dto.getProgram(), dto.getSeparator(), dto.getBlank());
        return tm.runSimulation(dto.getInput());
    }

}
