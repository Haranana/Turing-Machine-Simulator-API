package com.hubosm.turingsimulator.repositories;

import com.hubosm.turingsimulator.domain.TuringMachineSimulator;
import com.hubosm.turingsimulator.entities.TuringMachine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TuringMachineRepository extends JpaRepository<TuringMachine, Long> {
    boolean existsByAuthorIdAndNameIgnoreCaseAndIdNot(Long authorId, String name, Long id);
    boolean existsByAuthorIdAndNameIgnoreCase(Long authorId, String name);
    Optional<TuringMachine> findByNameAndAuthor_Id(String name, Long authorId);
    List<TuringMachine> findAllByAuthor_Id(Long authorId);
    Page<TuringMachine> findAllByAuthor_Id(Long authorId, Pageable pageable);
}
