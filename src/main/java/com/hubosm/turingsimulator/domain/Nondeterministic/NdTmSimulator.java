package com.hubosm.turingsimulator.domain.Nondeterministic;

import com.hubosm.turingsimulator.domain.*;
import com.hubosm.turingsimulator.dtos.NdTmReturnDto;
import com.hubosm.turingsimulator.dtos.NonDetSimulationDto;
import com.hubosm.turingsimulator.utils.Pair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.text.html.Option;
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
    private boolean rejectOnNonAccept;

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
        this.program = new NdTmProgram();


        for (String line : programLines) {
            if (line == null || line.trim().isEmpty()) continue;
            MultiTransition t = parseLine(line, sep1, sep2, tapesAmount);
            program.add(t);
        }

        this.tapes = new ArrayList<>();
        this.rejectOnNonAccept = rejectOnNonAccept;
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
        long stepId = 0L;
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

            NdTmStep step = new NdTmStep(i, stepId, action, read, write, this.initialState, stateAfter, tapeStateBefore);
            System.out.println("cur edge: " + curEdge);
            System.out.println("cur edge [i]: " + curEdge.getSteps().get(i));
            curEdge.getSteps().get(i).add(step);
            System.out.println("after edge");
        }
    }

    private void addSimulationStep(NdTmReturnDto out, NdTreeEdge curEdge, MultiTransition transition, State stateBefore){

        long stepId = curEdge.getSteps().get(0).size();
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

            NdTmStep step = new NdTmStep(i, stepId, action, read, write, stateBefore, stateAfter, tapeState);
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
            NdTreeEdge newEdge = new NdTreeEdge((long) out.getEdgeList().size(), tapesAmount);
            newNode.getEdgeIds().add(newEdge.getId());
            newEdge.setStartNodeId(newNode.getId());
            addedQueuesIds.add(Math.toIntExact(newEdge.getId()));
            out.getEdgeList().add(newEdge);

            //Create new Tapes object for new edge
            this.tapes.add(new Tapes(this.tapesAmount, this.blank));
            int tapesIndex = this.tapes.size() - 1;
            this.tapes.get(tapesIndex).placeInputs(inputs);
            newEdge.setTapesId(tapesIndex);

            addFirstimulationStep(out, newEdge, multiTransition);
        }

        return addedQueuesIds;
    }

    private void createLeaf(NdTmReturnDto out, NdTreeEdge currentEdge){

        NdTreeNode newNode = new NdTreeNode((long) out.getNodeList().size());
        out.getNodeList().add(newNode);
        currentEdge.setEndNodeId(newNode.getId());

    }

    private List<Integer> createNode(NdTmReturnDto out, NdTreeEdge currentEdge, List<MultiTransition> transitionList, State stateBefore){
        List<Integer> addedQueuesIds = new ArrayList<>();

        //create new node
        NdTreeNode newNode = new NdTreeNode((long) out.getNodeList().size());
        out.getNodeList().add(newNode);
        currentEdge.setEndNodeId(newNode.getId());

        //for each transition crate a new edge, assigned to newly created node
        for (MultiTransition multiTransition : transitionList) {

            //IDs assignment
            NdTreeEdge newEdge = new NdTreeEdge((long) out.getEdgeList().size(), tapesAmount);
            newNode.getEdgeIds().add(newEdge.getId());
            newEdge.setStartNodeId(newNode.getId());
            addedQueuesIds.add(Math.toIntExact(newEdge.getId()));
            out.getEdgeList().add(newEdge);

            //Create new Tapes object for new edge
            this.tapes.add( new Tapes(tapes.get(currentEdge.getTapesId())) );
            newEdge.setTapesId(tapes.size()-1);

            addSimulationStep(out, newEdge, multiTransition, stateBefore);
        }

        return addedQueuesIds;
    }

    //creates root and first branch(es) to put on queue
    private List<Integer> startSimulation(NdTmReturnDto out ,Tapes startingTapes, State startingState, List<String> inputs){

        char[] rc = startingTapes.readHeads();
        String[] readCharactes = new String[tapesAmount];
        for (int i=0;i<tapesAmount;i++)
            readCharactes[i] = String.valueOf(rc[i]);

        List<MultiTransition> tr = program.get(startingState, readCharactes).orElseThrow(() -> new RuntimeException(
                "No transition for state=" + startingState.name() + " reads=" + String.join(",", readCharactes)));

        if(tr.isEmpty()){
            throw new RuntimeException(
                    "No transition for state=" + startingState.name() + " reads=" + String.join(",", readCharactes));
        }

        System.out.println("transition: " + tr.get(0));
        return createRoot(out, tr, inputs);
    }

    //TODO, when step limit is reached simulation will leaf only current branch
    //TODO also gloabalStepCounter adds step only on simple branch addition not on node creation, FIX IT!!!!!

    public NdTmReturnDto runSimulation(List<String> inputs){

        //create dto, prepare function range values
        NdTmReturnDto out = new NdTmReturnDto();
        final int maxSteps = SimulationConfig.maxSteps;
        int globalStepCounter = 0;

        //prepare starting tape
        Tapes startingTape = new Tapes(tapesAmount, blank);
        startingTape.placeInputs(inputs);

        //create root and first branches to work on
        List<Integer> startingBranchesIds = startSimulation(out, startingTape, initialState, inputs);
        Queue<Integer> simulationQueue = new ArrayDeque<>(startingBranchesIds);

        while (!simulationQueue.isEmpty()){

            int currentEdgeId = simulationQueue.poll();
            NdTreeEdge currentEdge = out.getEdgeList().get(currentEdgeId);

            //current state is state after previous step
            State currentState = currentEdge.getSteps().get(0).get(currentEdge.getSteps().get(0).size()-1).stateAfter();

            char[] rc;
            rc = tapes.get(currentEdge.getTapesId()).readHeads();
            String[] readCharacters = new String[tapesAmount];
            for (int i=0;i<tapesAmount;i++)
                readCharacters[i] = String.valueOf(rc[i]);


            if(currentState.equals(acceptState) || currentState.equals(rejectState)){
                createLeaf(out, currentEdge);
                continue;
            }
            //try to find full transition(s) given the left side
            State finalCurrentState = currentState;
            List<MultiTransition> tr = program.get(currentState, readCharacters).orElseThrow(() -> new RuntimeException(
                    "No transition for state=" + finalCurrentState.name() + " reads=" + String.join(",", readCharacters)));

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
                        "No transition for state=" + currentState.name() + " reads=" + String.join(",", readCharacters));
            }
            else if(globalStepCounter >= maxSteps ){
                createLeaf(out, currentEdge);
                simulationQueue.clear();
                break;
            }
            else if(tr.size() == 1){
                System.out.println("transition: " + tr.get(0));
                addSimulationStep(out, currentEdge, tr.get(0), currentState);
                globalStepCounter+=1;
                simulationQueue.add(currentEdgeId);
            }else {
                List<Integer> edgesToAdd = createNode(out, currentEdge, tr, currentState);
                simulationQueue.addAll(edgesToAdd);
            }
        }
        return out;
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

            Optional<List<MultiTransition>> optionalMultiTransitions = program.get(currentState, readCharacters);
            if (optionalMultiTransitions.isEmpty()) {
                if(this.rejectOnNonAccept) {
                    currentNode.setOutput("REJECT");
                }
                else {
                    currentNode.setOutput("HALT");
                }
                continue;
            }
            List<MultiTransition> tr = optionalMultiTransitions.get();

            //for each transition a node is created alongside with tape after move specified in transition
            //newly created node has assigned previousID as id of current node
            State finalCurrentState1 = currentState;
            tr.forEach((transition)->{
                final String[] writtenCharacters = transition.getWrite();
                final MultiTransition.TransitionAction[] actions = transition.getActions();
                final State nextState = transition.getNextState();

                //create tape and node, add new node to map, add relation beetwen current and new node
                Tapes newTapes = new Tapes(currentTapes);
                SimulationNode newNode = new SimulationNode(out.getNodes().size());
                out.getNodes().put(newNode.getId(), newNode);
                newNode.setPrevId(currentNodeId);
                out.getNodes().get(currentNodeId).getNextIds().add(newNode.getId());


                List<FullSimulationStep> newNodeSimulationSteps = new ArrayList<>();
                //update Tapes and collect eachTapeState
                for(int tapeId = 0; tapeId < tapesAmount; tapeId++){
                    TapeState tapeState = newTapes.get(tapeId).ToTapeState(true);
                    newTapes.get(tapeId).writeOnHead(writtenCharacters[tapeId]);
                    switch (actions[tapeId]) {
                        case LEFT -> newTapes.get(tapeId).moveHeadLeft();
                        case RIGHT -> newTapes.get(tapeId).moveHeadRight();
                        case STAY -> {}
                    }
                    newNodeSimulationSteps.add(new FullSimulationStep(tapeId, actions[tapeId], readCharacters[tapeId], writtenCharacters[tapeId], finalCurrentState1, nextState, tapeState));
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
