package com.hubosm.turingsimulator.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode
@AllArgsConstructor
public class MultiTransitionKey {
    private final String state;
    private final List<String> reads;
    public static MultiTransitionKey of(State s, String[] reads){
        return new MultiTransitionKey(s.name(), List.of(reads));
    }
}
