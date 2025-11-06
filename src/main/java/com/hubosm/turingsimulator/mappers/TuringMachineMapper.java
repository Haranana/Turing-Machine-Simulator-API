package com.hubosm.turingsimulator.mappers;

import com.hubosm.turingsimulator.dtos.TuringMachineCreateDto;
import com.hubosm.turingsimulator.dtos.TuringMachineReturnDto;
import com.hubosm.turingsimulator.dtos.UserCreateDto;
import com.hubosm.turingsimulator.dtos.UserReturnDto;
import com.hubosm.turingsimulator.entities.TuringMachine;
import com.hubosm.turingsimulator.entities.User;
import com.hubosm.turingsimulator.repositories.TuringMachineRepository;
import com.hubosm.turingsimulator.repositories.UserRepository;
import com.hubosm.turingsimulator.utils.AccountStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class TuringMachineMapper {

    final UserRepository userRepository;
    final TuringMachineRepository turingMachineRepository;

    public TuringMachine CreateDtoToEntity(TuringMachineCreateDto dto) throws Exception {
        User machineAuthor = userRepository.findById(dto.getAuthorId()).orElseThrow(()->new Exception("user not found"));
        String trimmedName = dto.getName().trim();
        return TuringMachine.builder().author(machineAuthor).name(trimmedName).description(dto.getDescription())
                .program(dto.getProgram()).initialState(dto.getInitialState())
                .acceptState(dto.getAcceptState()).rejectState(dto.getRejectState())
                .blank(dto.getBlank()).sep1(dto.getSep1()).sep2(dto.getSep2()).tapesAmount(dto.getTapesAmount()).build();

    }

    public TuringMachineReturnDto EntityToReturnDto(TuringMachine entity){
        return TuringMachineReturnDto.builder().id(entity.getId()).authorId(entity.getAuthor().getId())
                .name(entity.getName()).description(entity.getDescription())
                .program(entity.getProgram()).initialState(entity.getInitialState())
                .acceptState(entity.getAcceptState()).rejectState(entity.getRejectState())
                .blank(entity.getBlank()).sep1(entity.getSep1())
                .sep2(entity.getSep2()).tapesAmount(entity.getTapesAmount())
                .createdAt(entity.getCreatedAt()).updatedAt(entity.getUpdatedAt()).build();
    }

}
