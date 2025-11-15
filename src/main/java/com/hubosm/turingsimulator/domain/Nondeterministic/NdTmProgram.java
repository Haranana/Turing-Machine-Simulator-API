package com.hubosm.turingsimulator.domain.Nondeterministic;

import com.hubosm.turingsimulator.domain.MultiTransition;
import com.hubosm.turingsimulator.domain.MultiTransitionKey;
import com.hubosm.turingsimulator.domain.State;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NdTmProgram {
    private final Map<MultiTransitionKey, List<MultiTransition>> map = new HashMap<>();
    public void add(MultiTransition t){
        MultiTransitionKey key = MultiTransitionKey.of(t.getCurrentState(), t.getRead());
        if(map.containsKey(key)){
            map.get(key).add(t);
        }else{
            map.put(key, List.of(t));
        }

    }
    public Optional<List<MultiTransition>> get(State s, String[] reads){
        return Optional.ofNullable(map.get(MultiTransitionKey.of(s, reads)));
    }
}
