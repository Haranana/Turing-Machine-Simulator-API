package com.hubosm.turingsimulator.mappers;

import com.hubosm.turingsimulator.domain.TuringMachine;
import com.hubosm.turingsimulator.dtos.CreateTuringMachineDto;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class TuringMachineMapper {
    public TuringMachine createDtoToDomain(CreateTuringMachineDto dto){
        return new TuringMachine(dto.getInitialState(),
                dto.getAcceptState(),
                dto.getRejectState(),
                dto.getProgram(),
                dto.getSeparator(),
                "_");
    }
}
