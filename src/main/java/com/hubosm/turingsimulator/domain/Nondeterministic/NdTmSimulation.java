package com.hubosm.turingsimulator.domain.Nondeterministic;

import java.util.ArrayList;
import java.util.List;

public class NdTmSimulation {
    List<NdTreeNode> nodeList;
    List<NdTreeEdge> edgeList;

    public NdTmSimulation(){
        nodeList = new ArrayList<>();
        edgeList = new ArrayList<>();
    }
}
