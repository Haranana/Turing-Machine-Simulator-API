package com.hubosm.turingsimulator.services;

import com.hubosm.turingsimulator.dtos.TuringMachineCreateDto;
import com.hubosm.turingsimulator.dtos.TuringMachineEditDto;
import com.hubosm.turingsimulator.dtos.TuringMachineReturnDto;
import com.hubosm.turingsimulator.entities.TuringMachine;
import com.hubosm.turingsimulator.exceptions.AccessDeniedException;
import com.hubosm.turingsimulator.exceptions.ElementNotFoundException;
import com.hubosm.turingsimulator.exceptions.IntegrityException;
import com.hubosm.turingsimulator.mappers.TuringMachineMapper;
import com.hubosm.turingsimulator.repositories.TuringMachineRepository;
import com.hubosm.turingsimulator.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TuringMachineServiceImpl implements TuringMachineService{

    final TuringMachineRepository turingMachineRepository;
    final TuringMachineMapper turingMachineMapper;
    final UserRepository userRepository;

    @Override
    public TuringMachineReturnDto getTuringMachine(Long id) throws Exception {
        TuringMachine entity = turingMachineRepository.findById(id).orElseThrow(()->new ElementNotFoundException("Turing machine not found"));
        return turingMachineMapper.EntityToReturnDto(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TuringMachineReturnDto> getTuringMachinesByUserId(Long id, Pageable pageable) {
        Page<TuringMachine> page = turingMachineRepository.findAllByAuthor_Id(id, pageable);
        List<TuringMachineReturnDto> content = page.getContent()
                .stream().map(turingMachineMapper::EntityToReturnDto).toList();
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }

    @Override
    public TuringMachineReturnDto editTuringMachine(TuringMachineEditDto dto) throws Exception {
        TuringMachine entity = turingMachineRepository
                .findById(dto.getId())
                .orElseThrow(() -> new ElementNotFoundException("Turing machine not found"));


        if (dto.getName() != null) {
            String newName = dto.getName().trim();
            boolean taken = turingMachineRepository
                    .existsByAuthorIdAndNameAndIdNot(entity.getAuthor().getId(), newName, entity.getId());
            if (taken) {
                throw new IntegrityException("Machine with this name already exists");
            }
            entity.setName(newName);
        }

        if (dto.getDescription() != null) {
            String v = dto.getDescription().trim();
            entity.setDescription(v.isEmpty() ? null : v);
        }

        if (dto.getProgram() != null) {
            entity.setProgram(dto.getProgram());
        }

        if (dto.getInitialState() != null) {
            entity.setInitialState(dto.getInitialState().trim());
        }
        if (dto.getAcceptState() != null) {
            String v = dto.getAcceptState().trim();
            entity.setAcceptState(v.isEmpty() ? null : v);
        }
        if (dto.getRejectState() != null) {
            String v = dto.getRejectState().trim();
            entity.setRejectState(v.isEmpty() ? null : v);
        }

        if (dto.getBlank() != null) {
            entity.setBlank(dto.getBlank().trim());
        }
        if (dto.getSep1() != null) {
            entity.setSep1(dto.getSep1().trim());
        }
        if (dto.getSep2() != null) {
            entity.setSep2(dto.getSep2().trim());
        }
        if (dto.getMoveRight() != null) {
            entity.setMoveRight(dto.getMoveRight().trim());
        }
        if (dto.getMoveLeft() != null) {
            entity.setMoveLeft(dto.getMoveLeft().trim());
        }
        if (dto.getMoveStay() != null) {
            entity.setMoveStay(dto.getMoveStay().trim());
        }

        if (dto.getTapesAmount() != null) {
            Integer ta = dto.getTapesAmount();
            if (ta < 1) throw new IllegalArgumentException("tapesAmount must be >= 1");
            entity.setTapesAmount(ta);
        }

        return turingMachineMapper.EntityToReturnDto(turingMachineRepository.save(entity));
    }

    @Override
    public void deleteTuringMachine(Long id , Long requestSenderId) throws Exception {

        System.out.println("id: " + id + " :  req id:" + requestSenderId );
        TuringMachine entity = turingMachineRepository
                .findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Turing machine not found"));
        if(!entity.getAuthor().getId().equals(requestSenderId)){
            throw new AccessDeniedException("Unauthorized user");
        }
        turingMachineRepository.deleteById(id);
    }

    @Override
    public TuringMachineReturnDto addTuringMachine(TuringMachineCreateDto dto, Long authorId) throws Exception{

        if(!userRepository.existsById(authorId)) throw new ElementNotFoundException("User not found");
        if (dto.getName() != null) {
            boolean taken = turingMachineRepository
                    .existsByAuthorIdAndName(authorId, dto.getName().trim());
            if (taken) {
                throw new IntegrityException("Machine with this name already exists");
            }

        }
        return turingMachineMapper.EntityToReturnDto(turingMachineRepository.save(turingMachineMapper.CreateDtoToEntity(dto, authorId)));
    }
}
