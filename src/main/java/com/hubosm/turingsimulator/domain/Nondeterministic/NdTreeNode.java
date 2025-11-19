package com.hubosm.turingsimulator.domain.Nondeterministic;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class NdTreeNode {
    private Long id;
    private List<Long> edgeIds;

    public NdTreeNode(Long id){
        edgeIds = new ArrayList<>();
        this.id = id;
    }
}
