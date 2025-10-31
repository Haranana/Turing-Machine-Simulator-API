package com.hubosm.turingsimulator.services;

import com.hubosm.turingsimulator.domain.*;
import com.hubosm.turingsimulator.dtos.*;
import com.hubosm.turingsimulator.exceptions.TuringMachineException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

@Service
@RequiredArgsConstructor
public class SimulationServiceImpl implements SimulationService{

    private final static Duration DEFAULT_TTL = Duration.ofHours(2);
    private final BlockingQueue<UUID> jobQueue;
    private final CacheServiceImpl cacheService;


    public UUID queueSimulation(CreateTuringMachineDto dto){
        UUID jobId = UUID.randomUUID();

        cacheService.saveDefObject(jobId, dto, DEFAULT_TTL);
        cacheService.saveAllHash(jobId,
                Map.of("status" , "QUEUED",
                        "timestamp",    Instant.now().toString()));
        cacheService.setTTL(jobId , "meta", DEFAULT_TTL);
        jobQueue.add(jobId);
        return jobId;
    }

    public SimulationStatusDto getStatus(UUID jobId){
        return new SimulationStatusDto((String)cacheService.getHash(jobId,"meta","status"));
    }

    public List<SimulationStepDto> getSteps(UUID jobId, int offset, int limit){
       return cacheService.getList(jobId, "steps").stream().skip(offset).limit(limit).map(o->(SimulationStepDto)o).toList();
    }

    public CreatedSimulationDto runSimulation(CreateSimulationDto dto){
        TuringMachine tm = new TuringMachine(dto.getInitialState(), dto.getAcceptState(), dto.getRejectState(), dto.getProgram(), dto.getSeparator());
        return tm.runSimulation(dto.getInput());
    }

}
