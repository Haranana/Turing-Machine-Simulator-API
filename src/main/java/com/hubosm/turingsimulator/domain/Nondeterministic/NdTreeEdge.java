package com.hubosm.turingsimulator.domain.Nondeterministic;

import com.hubosm.turingsimulator.domain.FullSimulationStep;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class NdTreeEdge {
    private Long id;
    //id of tapes object used in simulation
    //should be ignored by app and ideally removed from dto
    private int tapesId;
    private List<List<NdTmStep>> steps;
    private Long startNodeId;
    private Long endNodeId;

    public NdTreeEdge(Long id, int tapesAmount){
        steps = new ArrayList<>();
        for(int i=0; i<tapesAmount; i++){
            steps.add(new ArrayList<>());
        }
        this.id = id;
    }
}
