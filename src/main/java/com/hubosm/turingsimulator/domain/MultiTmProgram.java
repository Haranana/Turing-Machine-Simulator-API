package com.hubosm.turingsimulator.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

public class MultiTmProgram {
    private final Map<MultiTransitionKey, MultiTransition> map = new HashMap<>();
    public void add(MultiTransition t){
        map.put(MultiTransitionKey.of(t.getCurrentState(), t.getRead()), t);
    }
    public Optional<MultiTransition> get(State s, String[] reads){
        return Optional.ofNullable(map.get(MultiTransitionKey.of(s, reads)));
    }

    private static MultiTransition parseLine(String line, String sep1, String sep2, int tapes) {
        String s1 = Pattern.quote(sep1);
        String s2 = Pattern.quote(sep2);

        String trimmed = line.replaceAll("\\s+","");
        String[] halves = trimmed.split(s2, -1);
        if (halves.length != 2) throw new RuntimeException("Bad transition (sep2): " + line);

        String[] L = halves[0].split(s1, -1);


        String[] R = halves[1].split(s1, -1);

        if (L.length != 1 + tapes) throw new RuntimeException("Bad left arity: " + line);
        if (R.length != 1 + tapes + tapes) throw new RuntimeException("Bad right arity: " + line);

        State cur = new State(L[0]);

        String[] reads = new String[tapes];
        for (int i=0;i<tapes;i++)
            reads[i] = L[1+i];

        State next = new State(R[0]);

        String[] writes = new String[tapes];
        for (int i=0;i<tapes;i++)
            writes[i] = R[1+i];

        MultiTransition.TransitionAction[] actions = new MultiTransition.TransitionAction[tapes];
        for (int i=0;i<tapes;i++) {
            String m = R[1 + tapes + i];

            if ("L".equalsIgnoreCase(m)) m = "LEFT";
            else if ("R".equalsIgnoreCase(m)) m = "RIGHT";
            else if ("S".equalsIgnoreCase(m)) m = "STAY";
            actions[i] = MultiTransition.stringToAction(m);
        }

        return new MultiTransition(cur, reads, next, writes, actions);
    }
}
