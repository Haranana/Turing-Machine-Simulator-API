package com.hubosm.turingsimulator.repositories;

import com.hubosm.turingsimulator.entities.TuringMachine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TuringMachineRepository extends JpaRepository<TuringMachine, Long> {
    Optional<TuringMachine> findByAuthorIdAndNameAndIdNot(Long authorId, String name, Long id);
    Optional<TuringMachine> findByNameAndAuthor_Id(String name, Long authorId);
    Page<TuringMachine> findAllByAuthor_Id(Long authorId, Pageable pageable);
    Optional<TuringMachine> findByShareCode(String shareCode);
    boolean existsByShareCode(String shareCode);
}
