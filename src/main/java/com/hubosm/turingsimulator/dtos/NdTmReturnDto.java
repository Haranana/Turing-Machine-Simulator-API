package com.hubosm.turingsimulator.dtos;

import com.hubosm.turingsimulator.domain.Nondeterministic.NdTreeEdge;
import com.hubosm.turingsimulator.domain.Nondeterministic.NdTreeNode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class NdTmReturnDto {
    List<NdTreeNode> nodeList;
    List<NdTreeEdge> edgeList;

    public NdTmReturnDto(){
        nodeList = new ArrayList<>();
        edgeList = new ArrayList<>();
    }
}
