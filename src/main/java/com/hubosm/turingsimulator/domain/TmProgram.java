package com.hubosm.turingsimulator.domain;

import java.util.*;

public class TmProgram {
    private final Map<TransitionKey, List<Transition>> map = new HashMap<>();

    public void add(Transition t){
        TransitionKey key = TransitionKey.of(t.getCurrentState(), t.getRead());
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(t);
    }

    public Optional<List<Transition>> get(State s, String[] reads){
        return Optional.ofNullable(map.get(TransitionKey.of(s, reads)));
    }
}
