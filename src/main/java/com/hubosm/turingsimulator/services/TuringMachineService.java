package com.hubosm.turingsimulator.services;

import com.hubosm.turingsimulator.dtos.TuringMachineCreateDto;
import com.hubosm.turingsimulator.dtos.TuringMachineEditDto;
import com.hubosm.turingsimulator.dtos.TuringMachineReturnDto;
import com.hubosm.turingsimulator.entities.TuringMachine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TuringMachineService {

    TuringMachineReturnDto getTuringMachine(Long id) throws Exception;
    Page<TuringMachineReturnDto> getTuringMachinesByUserId(Long id, Pageable pageable);

    TuringMachineReturnDto editTuringMachine(TuringMachineEditDto dto) throws Exception;

    void deleteTuringMachine(Long id) throws Exception;

    TuringMachineReturnDto addTuringMachine(TuringMachineCreateDto dto, Long authorId) throws Exception;
}
