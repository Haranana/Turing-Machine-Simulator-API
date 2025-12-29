package com.hubosm.turingsimulator.domain;

import com.hubosm.turingsimulator.dtos.NonDetSimulationDto;
import com.hubosm.turingsimulator.utils.Pair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;
import java.util.regex.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class TmSimulator {

    private State initialState;
    private  State acceptState;
    private  State rejectState;

    private  String sep1;
    private  String sep2;
    private  String blank;

    private  int tapesAmount;
    private TmProgram program;
    private boolean rejectOnNonAccept;

    //1 Tapes object per edge
    private List<Tapes> tapes;

    public TmSimulator(
            String initialState,
            String acceptState,
            String rejectState,
            Collection<String> programLines,
            String sep1,
            String sep2,
            String blank,
            int tapesAmount,
            boolean rejectOnNonAccept

    ) {
        this.initialState = new State(initialState);
        this.acceptState = new State(acceptState);
        this.rejectState = new State(rejectState);

        this.sep1 = sep1;
        this.sep2 = sep2;
        this.blank = blank;
        this.tapesAmount = tapesAmount;
        this.program = new TmProgram();


        for (String line : programLines) {
            if (line == null || line.trim().isEmpty()) continue;
            Transition t = parseLine(line, sep1, sep2, tapesAmount);
            program.add(t);
        }

        this.tapes = new ArrayList<>();
        this.rejectOnNonAccept = rejectOnNonAccept;
    }

    public NonDetSimulationDto createSimulation(List<String> inputs){
        //create dto, prepare function range values
        NonDetSimulationDto out = new NonDetSimulationDto();
        final int maxSteps = SimulationConfig.maxSteps;

        //prepare starting tape
        Tapes startingTape = new Tapes(tapesAmount, blank);
        startingTape.placeInputs(inputs);

        //queue stores nodeId and Tapes Object for this nodeId
        Queue<Pair<Integer, Tapes>> simulationQueue = new ArrayDeque<>();
        out.getNodes().put(0, new SimulationNode(0));
        simulationQueue.add(new Pair<>(0, startingTape));
        State currentState = initialState;
        boolean stepLimitExceeded = false;

        while (!simulationQueue.isEmpty()){

            if(out.getNodes().size() > maxSteps){
                stepLimitExceeded = true;
                simulationQueue.clear();
                break;
            }

            //get data from queue
            Pair<Integer, Tapes> simulationElement = simulationQueue.poll();
            int currentNodeId = simulationElement.key();
            Tapes currentTapes = simulationElement.value();
            SimulationNode currentNode = out.getNodes().get(currentNodeId);

            if (currentNodeId == 0 || currentNode.getStep().isEmpty()) {
                currentState = initialState;
            } else {
                currentState = currentNode.getStep().get(0).stateAfter();
            }

            if (currentState.equals(acceptState)) {
                currentNode.setOutput("ACCEPT");
                continue;
            }

            if (rejectState != null && currentState.equals(rejectState)) {
                currentNode.setOutput("REJECT");
                continue;
            }

            //read characters on heads
            char[] rc;
            rc = currentTapes.readHeads();
            String[] readCharacters = new String[tapesAmount];
            for (int i=0;i<tapesAmount;i++)
                readCharacters[i] = String.valueOf(rc[i]);

            //try to find full transition(s) given the left side
            //State finalCurrentState = currentState;
            //List<MultiTransition> tr = program.get(currentState, readCharacters).orElseThrow(() -> new RuntimeException(
             //       "No transition for state=" + finalCurrentState.name() + " reads=" + String.join(",", readCharacters)));

            Optional<List<Transition>> optionalMultiTransitions = program.get(currentState, readCharacters);
            if (optionalMultiTransitions.isEmpty()) {
                if(this.rejectOnNonAccept) {
                    currentNode.setOutput("REJECT");
                }
                else {
                    currentNode.setOutput("HALT");
                }
                continue;
            }
            List<Transition> tr = optionalMultiTransitions.get();

            //for each transition a node is created alongside with tape after move specified in transition
            //newly created node has assigned previousID as id of current node
            State finalCurrentState1 = currentState;
            tr.forEach((transition)->{
                final String[] writtenCharacters = transition.getWrite();
                final Transition.TransitionAction[] actions = transition.getActions();
                final State nextState = transition.getNextState();

                //create tape and node, add new node to map, add relation beetwen current and new node
                Tapes newTapes = new Tapes(currentTapes);
                SimulationNode newNode = new SimulationNode(out.getNodes().size());
                out.getNodes().put(newNode.getId(), newNode);
                newNode.setPrevId(currentNodeId);
                out.getNodes().get(currentNodeId).getNextIds().add(newNode.getId());


                List<SimulationStep> newNodeSimulationSteps = new ArrayList<>();
                //update Tapes and collect eachTapeState
                for(int tapeId = 0; tapeId < tapesAmount; tapeId++){
                    TapeState tapeState = newTapes.get(tapeId).ToTapeState(true);
                    newTapes.get(tapeId).writeOnHead(writtenCharacters[tapeId]);
                    switch (actions[tapeId]) {
                        case LEFT -> newTapes.get(tapeId).moveHeadLeft();
                        case RIGHT -> newTapes.get(tapeId).moveHeadRight();
                        case STAY -> {}
                    }
                    newNodeSimulationSteps.add(new SimulationStep(tapeId, actions[tapeId], readCharacters[tapeId], writtenCharacters[tapeId], finalCurrentState1, nextState, tapeState));
                }
                newNode.setStep(newNodeSimulationSteps);

                simulationQueue.add(new Pair<>(newNode.getId(), newTapes));
            });
        }

        //if there were no steps made (root is the only node), root also should be deleted
        //but if there's halt on root there should be some info on this
        if (out.getNodes().size() == 1) {
            SimulationNode root = out.getNodes().get(0);
            if (root.getNextIds().isEmpty() && root.getOutput() == null) {
                out.getNodes().clear();
            }
        }

        //if step limit exceeded leafs should have their output set as "LIMIT"
        if(stepLimitExceeded) {
            for (Map.Entry<Integer, SimulationNode> entry : out.getNodes().entrySet()) {
                if (entry.getValue().getNextIds().isEmpty() && entry.getValue().getOutput() == null) {
                    if(this.rejectOnNonAccept){
                        entry.getValue().setOutput("REJECT");
                    }else{
                        entry.getValue().setOutput("LIMIT");
                    }
                }
            }
        }
        return out;
    }


    private static Transition parseLine(String line, String sep1, String sep2, int tapes) {
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

        Transition.TransitionAction[] actions = new Transition.TransitionAction[tapes];
        for (int i=0;i<tapes;i++) {
            String m = R[1 + tapes + i];

            if ("L".equalsIgnoreCase(m)) m = "LEFT";
            else if ("R".equalsIgnoreCase(m)) m = "RIGHT";
            else if ("S".equalsIgnoreCase(m)) m = "STAY";
            actions[i] = Transition.stringToAction(m);
        }

        return new Transition(cur, reads, next, writes, actions);
    }
}
