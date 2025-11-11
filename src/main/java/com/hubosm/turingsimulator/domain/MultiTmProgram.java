package com.hubosm.turingsimulator.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MultiTmProgram {
    private final Map<MultiTransitionKey, MultiTransition> map = new HashMap<>();
    public void add(MultiTransition t){ map.put(MultiTransitionKey.of(t.getCurrentState(), t.getRead()), t); }
    public Optional<MultiTransition> get(State s, String[] reads){ return Optional.ofNullable(map.get(MultiTransitionKey.of(s, reads))); }
}
