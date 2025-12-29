package com.hubosm.turingsimulator.domain;

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
    List<SimulationStep> steps = new ArrayList<>();
    State stateBefore = null;
    State stateAfter = null;
    String output = null;
}
