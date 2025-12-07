package com.hubosm.turingsimulator.domain;

import com.hubosm.turingsimulator.domain.Nondeterministic.NdTmStep;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class SimulationNode {
    final int id;
    Integer prevId = null;
    List<Integer> nextIds = new ArrayList<>();
    List<FullSimulationStep> step = new ArrayList<>();
    String output = null;
}
