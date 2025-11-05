package com.hubosm.turingsimulator.services;

/*
@Component
@RequiredArgsConstructor
public class SimulationWorkerImpl implements Runnable, SimulationWorker {

    private final BlockingQueue<UUID> jobQueue;

    private final SimulationRunnerImpl simulationRunner;
    private final CacheServiceImpl cacheService;
    private static final Duration DEFAULT_TTL = Duration.ofHours(2);
    private static final int BATCH = 500;

    @PostConstruct
    void boot(){
        new Thread(this, "simulationWorker").start();
    }

    @Override
    public void run() {
        while(true) {
            UUID jobId = null;
            try {
                jobId = jobQueue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            List<SimulationStepDto> buffer = new ArrayList<>();
            try {
                CreateTuringMachineDto dto = (CreateTuringMachineDto) cacheService.getDefObject(jobId);
                if (dto == null) continue;
                cacheService.saveHash(jobId, "meta", "status", "RUNNING");


                UUID finalJobId = jobId;
                simulationRunner.run(dto, (stepId, stepDto) -> {
                    buffer.add(stepDto);
                    if (buffer.size() >= BATCH) {
                        flush(finalJobId, buffer);
                    }
                });
                flush(jobId, buffer);
                cacheService.saveAllHash(jobId, Map.of("status", "DONE", "timestamp", Instant.now().toString()));
                cacheService.setTTL(jobId, "meta", DEFAULT_TTL);
            } catch (Exception e) {
                flush(jobId, buffer);
                cacheService.saveAllHash(jobId, Map.of("status", "FAILED", "timestamp", Instant.now().toString()));
                cacheService.setTTL(jobId, "meta", DEFAULT_TTL);
            }
        }
    }

    @Override
    public void flush(UUID id, List<SimulationStepDto> batch){
        cacheService.saveAllList(id, "steps", batch);
        cacheService.setTTL(id, "steps", DEFAULT_TTL);
        batch.clear();
    }
}
*/