package com.hubosm.turingsimulator.services;

import com.hubosm.turingsimulator.dtos.TuringMachineCreateDto;
import com.hubosm.turingsimulator.dtos.TuringMachineEditDto;
import com.hubosm.turingsimulator.dtos.TuringMachineReturnDto;
import com.hubosm.turingsimulator.entities.TuringMachine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TuringMachineService {

    TuringMachineReturnDto getTuringMachine(Long id) throws Exception;
    Page<TuringMachineReturnDto> getTuringMachinesByUserId(Long id, Pageable pageable);

    TuringMachineReturnDto editTuringMachine(TuringMachineEditDto dto, Long requestSenderId) throws Exception;

    void deleteTuringMachine(Long id ,Long requestSenderId) throws Exception;

    TuringMachineReturnDto addTuringMachine(TuringMachineCreateDto dto, Long authorId) throws Exception;

    Optional<TuringMachineReturnDto> existsByNameAndAuthor(String turingMachineName, Long authorId) throws Exception;
}
