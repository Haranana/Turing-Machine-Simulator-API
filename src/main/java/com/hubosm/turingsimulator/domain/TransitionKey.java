package com.hubosm.turingsimulator.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode
@AllArgsConstructor
public class TransitionKey {
    private final String state;
    private final List<String> reads;
    public static TransitionKey of(State s, String[] reads){
        return new TransitionKey(s.name(), List.of(reads));
    }
}
