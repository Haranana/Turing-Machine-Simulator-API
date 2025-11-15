package com.hubosm.turingsimulator.domain;

import com.hubosm.turingsimulator.dtos.SimulationReturnDto;
import com.hubosm.turingsimulator.exceptions.TuringMachineException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;
import java.util.regex.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class TuringMachineSimulator {

    private  State initialState;
    private  State acceptState;
    private  State rejectState;

    private  String sep1;
    private  String sep2;
    private  String blank;

    private  int tapesAmount;
    private  MultiTmProgram program;
    private  Tapes tapes;

    public TuringMachineSimulator(
            String initialState,
            String acceptState,
            String rejectState,
            Collection<String> programLines,
            String sep1,
            String sep2,
            String blank,
            int tapesAmount
    ) {
        this.initialState = new State(initialState);
        this.acceptState  = new State(acceptState);
        this.rejectState  = new State(rejectState);

        this.sep1 = sep1;
        this.sep2 = sep2;
        this.blank = blank;
        this.tapesAmount = tapesAmount;

        this.program = new MultiTmProgram();
        for (String line : programLines) {
            if (line == null || line.trim().isEmpty()) continue;
            MultiTransition t = parseLine(line, sep1, sep2, tapesAmount);
            program.add(t);
        }

        this.tapes = new Tapes(tapesAmount, blank);
    }

    public SimulationReturnDto runSimulation(List<String> inputs) {
        tapes.placeInputs(inputs);

        SimulationReturnDto out = new SimulationReturnDto();
        out.steps = new ArrayList<>();

        //System.out.println(out.steps);
        for (int i = 0; i < tapesAmount; i++)
            out.steps.add(new ArrayList<>());

        State cur = initialState;
        int step = 0;
        int maxSteps = SimulationConfig.maxSteps;

        while (!cur.equals(acceptState) && !cur.equals(rejectState) && step < maxSteps) {
            step++;

            TapeState[] before = new TapeState[tapesAmount];
            for (int i=0;i<tapesAmount;i++)
                before[i] = tapes.get(i).ToTapeState(true);

            char[] rc = tapes.readHeads();
            String[] reads = new String[tapesAmount];
            for (int i=0;i<tapesAmount;i++)
                reads[i] = String.valueOf(rc[i]);

            State finalCur = cur;
            MultiTransition tr = program.get(cur, reads).orElseThrow(() -> new RuntimeException(
                            "No transition for state=" + finalCur.name() + " reads=" + String.join(",", reads)));

            for (int i=0;i<tapesAmount;i++){
                String write = tr.getWrite()[i];
                tapes.get(i).writeOnHead(write);
                switch (tr.getActions()[i]) {

                    case LEFT  -> tapes.get(i).moveHeadLeft();
                    case RIGHT -> tapes.get(i).moveHeadRight();
                    case STAY  -> {}
                }
                out.steps.get(i).add(new FullSimulationStep(i, tr.getActions()[i],
                        reads[i], write,
                        new State(cur.name()),
                        new State(tr.getNextState().name()),
                        before[i]
                ));
            }
            cur = tr.getNextState();
        }

        return out;
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