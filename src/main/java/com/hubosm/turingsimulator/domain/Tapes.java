package com.hubosm.turingsimulator.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Tapes {
    private final List<Tape> tapes;

    public Tapes(int n, String blank) {
        this.tapes = IntStream.range(0, n).mapToObj(i -> new Tape(blank)).toList();
    }

    public Tapes(Tapes other) {
        this.tapes = other.tapes.stream()
                .map(Tape::new)
                .collect(Collectors.toList());
    }
    public void placeInputs(List<String> inputs){
        for (int i=0;i<tapes.size();i++) tapes.get(i).placeText(inputs.get(i));
    }
    public char[] readHeads(){
        char[] r = new char[tapes.size()];
        for (int i=0;i<tapes.size();i++) r[i]=tapes.get(i).readHeadChar();
        return r;
    }
    public Tape get(int i){
        return tapes.get(i);
    }

    public int size(){
        return tapes.size();
    }

    public List<Tape> all(){
        return tapes;
    }
}
