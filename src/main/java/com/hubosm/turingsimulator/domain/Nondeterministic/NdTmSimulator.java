package com.hubosm.turingsimulator.domain.Nondeterministic;

import com.hubosm.turingsimulator.domain.*;
import com.hubosm.turingsimulator.dtos.NdTmReturnDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;
import java.util.regex.Pattern;

import static com.hubosm.turingsimulator.domain.MultiTransition.TransitionAction.*;

@Getter
@Setter
@NoArgsConstructor
public class NdTmSimulator {

    private State initialState;
    private  State acceptState;
    private  State rejectState;

    private  String sep1;
    private  String sep2;
    private  String blank;

    private  int tapesAmount;
    private NdTmProgram program;

    //1 Tapes object per edge
    private List<Tapes> tapes;

    public NdTmSimulator(
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
        this.acceptState = new State(acceptState);
        this.rejectState = new State(rejectState);

        this.sep1 = sep1;
        this.sep2 = sep2;
        this.blank = blank;
        this.tapesAmount = tapesAmount;
        this.program = new NdTmProgram();


        for (String line : programLines) {
            if (line == null || line.trim().isEmpty()) continue;
            MultiTransition t = parseLine(line, sep1, sep2, tapesAmount);
            program.add(t);
        }

        this.tapes = new ArrayList<>();
    }

    //returns step of given edge and id for every tape
    //in other words returns given column from edge
    //id refers to id of step inside edge
    private List<NdTmStep> getStep(NdTreeEdge curEdge ,int id){
        List<NdTmStep> out = new ArrayList<>();
        curEdge.getSteps().forEach((step)->{
            out.add(step.get(id));
        });
        return out;
    }

    private void addFirstimulationStep(NdTmReturnDto out, NdTreeEdge curEdge, MultiTransition transition){
        for (int i=0;i<tapesAmount;i++) {
            MultiTransition.TransitionAction action = transition.getActions()[i];
            String read = transition.getRead()[i];
            State stateAfter = transition.getNextState();
            TapeState tapeStateBefore = tapes.get(curEdge.getTapesId()).get(i).ToTapeState(true);
            String write = transition.getWrite()[i];
            tapes.get(curEdge.getTapesId()).get(i).writeOnHead(write);

            switch (action) {
                case LEFT -> tapes.get(curEdge.getTapesId()).get(i).moveHeadLeft();
                case RIGHT -> tapes.get(curEdge.getTapesId()).get(i).moveHeadRight();
                case STAY -> {}
            }

            NdTmStep step = new NdTmStep(i, 0L, action, read, write, this.initialState, stateAfter, tapeStateBefore);
            curEdge.getSteps().get(i).add(step);
        }
    }

    private void addSimulationStep(NdTmReturnDto out, NdTreeEdge curEdge, MultiTransition transition){
        for (int i=0;i<tapesAmount;i++) {
            MultiTransition.TransitionAction action = transition.getActions()[i];
            String read = transition.getRead()[i];
            State stateAfter = transition.getNextState();
            TapeState tapeState = tapes.get(curEdge.getTapesId()).get(i).ToTapeState(true);
            String write = transition.getWrite()[i];
            tapes.get(curEdge.getTapesId()).get(i).writeOnHead(write);

            switch (action) {
                case LEFT -> tapes.get(curEdge.getTapesId()).get(i).moveHeadLeft();
                case RIGHT -> tapes.get(curEdge.getTapesId()).get(i).moveHeadRight();
                case STAY -> {}
            }

            NdTmStep step = new NdTmStep(i, 0L, action, read, write, this.initialState, stateAfter, tapeState);
            curEdge.getSteps().get(i).add(step);
        }
    }

    //returns IDs of newly created edges to be added to queue
    private List<Integer> createRoot(NdTmReturnDto out, List<MultiTransition> transitionList, List<String> inputs){

        List<Integer> addedQueuesIds = new ArrayList<>();

        //create new node
        NdTreeNode newNode = new NdTreeNode((long) out.getNodeList().size());
        out.getNodeList().add(newNode);

        //for each transition crate a new edge, assigned to newly created node
        for (MultiTransition multiTransition : transitionList) {

            //IDs assignment
            NdTreeEdge newEdge = new NdTreeEdge((long) out.getEdgeList().size());
            newNode.getEdgeIds().add(newEdge.getId());
            newEdge.setStartNodeId(newNode.getId());
            addedQueuesIds.add(Math.toIntExact(newEdge.getId()));

            //Create new Tapes object for new edge
            this.tapes.add(new Tapes(this.tapesAmount, this.blank));
            this.tapes.getFirst().placeInputs(inputs);
            newEdge.setTapesId(tapes.size()-1);

            addFirstimulationStep(out, newEdge, multiTransition);
        }

        return addedQueuesIds;
    }

    private void createLeaf(NdTmReturnDto out, NdTreeEdge currentEdge){

        NdTreeNode newNode = new NdTreeNode((long) out.getNodeList().size());
        out.getNodeList().add(newNode);
        currentEdge.setEndNodeId(newNode.getId());

    }

    private List<Integer> createNode(NdTmReturnDto out, NdTreeEdge currentEdge, List<MultiTransition> transitionList){
        List<Integer> addedQueuesIds = new ArrayList<>();

        //create new node
        NdTreeNode newNode = new NdTreeNode((long) out.getNodeList().size());
        out.getNodeList().add(newNode);
        currentEdge.setEndNodeId(newNode.getId());

        //for each transition crate a new edge, assigned to newly created node
        for (MultiTransition multiTransition : transitionList) {

            //IDs assignment
            NdTreeEdge newEdge = new NdTreeEdge((long) out.getEdgeList().size());
            newNode.getEdgeIds().add(newEdge.getId());
            newEdge.setStartNodeId(newNode.getId());
            addedQueuesIds.add(Math.toIntExact(newEdge.getId()));

            //Create new Tapes object for new edge
            this.tapes.add( new Tapes(tapes.get(currentEdge.getTapesId())) );
            newEdge.setTapesId(tapes.size()-1);

            addSimulationStep(out, newEdge, multiTransition);
        }

        return addedQueuesIds;
    }

    private NdTmReturnDto runSimulation(List<String> inputs){

        NdTmReturnDto out = new NdTmReturnDto();
        Queue<Long> simulationQueue = new ArrayDeque<>();
        final int maxSteps = SimulationConfig.maxSteps;
        NdTreeEdge currentEdge = null;

        Tapes startingTape = new Tapes(tapesAmount, blank);
        State currentState = initialState;
        startingTape.placeInputs(inputs);
        int globalStepCounter = 0;

        do {
            Long currentEdgeId = simulationQueue.poll();
            currentEdge = currentEdgeId == null? null : out.getEdgeList().get(Math.toIntExact(currentEdgeId));

            char[] rc;
            if(currentEdge == null){
                rc = startingTape.readHeads();
            }else{
                rc = tapes.get(currentEdge.getTapesId()).readHeads();
            }
            String[] readCharactes = new String[tapesAmount];
            for (int i=0;i<tapesAmount;i++)
                readCharactes[i] = String.valueOf(rc[i]);

            //try to find full transition(s) given the left side
            State finalCurrentState = currentState;
            List<MultiTransition> tr = program.get(currentState, readCharactes).orElseThrow(() -> new RuntimeException(
                    "No transition for state=" + finalCurrentState.name() + " reads=" + String.join(",", readCharactes)));

            /*
            - if no transition is find - exception
            - if there's no node in simulation (or currentEdge is null which should only happen before root is created anyway)
            then create root
            - 1 transition found - simulate step and add it to current branch
            - if stepCounter is exceeded, leaf current edge and stop simulation
            - if accept or reject states are found then leaf current branch but continue other branches
            - more than one transition found - create nd tree node
            */

            if(tr.isEmpty()){
                throw new RuntimeException(
                        "No transition for state=" + currentState.name() + " reads=" + String.join(",", readCharactes));
            }
            else if(out.getNodeList().isEmpty() || currentEdge == null){
               List<Integer> edgesToAdd = createRoot(out, tr, inputs);
               edgesToAdd.forEach((edgeId)->{
                   simulationQueue.add(Long.valueOf(edgeId));
               });
            }else if(globalStepCounter >= maxSteps ){
                createLeaf(out, currentEdge);
                simulationQueue.clear();
                break;
            }else if(currentState.equals(acceptState) || currentState.equals(rejectState)){
                createLeaf(out, currentEdge);
            }
            else if(tr.size() == 1){
                addSimulationStep(out, currentEdge, tr.getFirst());
                globalStepCounter+=1;
            }else {
                List<Integer> edgesToAdd = createNode(out, currentEdge, tr);
                edgesToAdd.forEach((edgeId) -> {
                    simulationQueue.add(Long.valueOf(edgeId));
                });
            }

            currentState = currentEdge.getSteps().getFirst().getLast().stateAfter();


        }while (!simulationQueue.isEmpty());
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
