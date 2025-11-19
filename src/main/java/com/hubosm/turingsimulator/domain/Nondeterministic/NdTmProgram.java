package com.hubosm.turingsimulator.domain.Nondeterministic;

import com.hubosm.turingsimulator.domain.MultiTransition;
import com.hubosm.turingsimulator.domain.MultiTransitionKey;
import com.hubosm.turingsimulator.domain.State;

import java.util.*;

public class NdTmProgram {
    private final Map<MultiTransitionKey, List<MultiTransition>> map = new HashMap<>();
    public void add(MultiTransition t){
        MultiTransitionKey key = MultiTransitionKey.of(t.getCurrentState(), t.getRead());
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(t);

    }
    public Optional<List<MultiTransition>> get(State s, String[] reads){
        return Optional.ofNullable(map.get(MultiTransitionKey.of(s, reads)));
    }
}
