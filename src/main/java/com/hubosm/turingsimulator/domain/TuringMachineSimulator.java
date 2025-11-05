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

    private Set<State> states;
    private Set<Character> inputAlphabet;
    private State initialState;
    private State acceptState;
    private State rejectState;
    private Set<Character> tapeAlphabet;
    private TmProgram program;
    private Tape tape;
    private String blank;

    public TuringMachineSimulator(String initialState, String acceptState, String rejectState, Collection<String> program, Character separator, String blank){
        String regex    = Pattern.quote(String.valueOf(separator));
        this.states = new HashSet<>();
        this.tape = new Tape(blank);
        this.program = new TmProgram();
        this.acceptState = new State(acceptState);
        this.rejectState = new State(rejectState);
        this.initialState = new State(initialState);
        this.blank = blank;
        states.add(this.acceptState);
        states.add(this.initialState);

        try {
            for (String trans : program) {
                //in future there may be different separator beetwen left and right side of Transition, so here regex is used for split instead of simply using separator
                System.out.println("before: " +  trans);
                List<String> parts = List.of(trans.replaceAll(" ", "").split(regex, -1));

                State currentState = new State(parts.get(0));
                String readSymbol = parts.get(1);
                State nextState = new State(parts.get(2));
                String writeSymbol = parts.get(3);
                String action = parts.get(4);

                states.add(currentState);
                states.add(nextState);

                this.program.addTransition(new Transition(currentState, readSymbol.charAt(0), nextState, writeSymbol.charAt(0), Transition.stringToAction(action)));
                System.out.println("after: " +  this.program.getTransitions().get(this.program.getTransitions().size()-1));
            }
        } catch (Exception e) {
            throw new TuringMachineException("Error in loading transitions");
        }
    }

    public SimulationReturnDto runSimulation(String input){



        SimulationReturnDto output = new SimulationReturnDto();
        output.steps = new ArrayList<>();
        output.steps.add(new ArrayList<>());

        int maxSteps = SimulationConfig.maxSteps;
        int curStep = 0;

        tape.placeText(input);

        State currentState = initialState;
        char readChar;
        Transition currentTransition;

        do {

            //Initialize variables to be saved in dto

            int stepTapeIndex = 0;
            String stepAction;
            String stepWrittenChar;
            String stepStateBefore;
            String stepStateAfter;
            Map<Integer, String> tapeBefore;
            TapeState tapeState = tape.ToTapeState(true);

            //Find whole Transition based on current state and character read by head
            curStep++;
            readChar = tape.readHeadChar();
            try {
                currentTransition = program.getTransitionByLeftSide(currentState, readChar).orElseThrow();
            } catch (Exception e) {
                throw new TuringMachineException("call to non-existing transition from: " + currentState.name() + " ; " + readChar);
            }

            //Write character under head based on Transition
            stepWrittenChar = String.valueOf(currentTransition.getWriteSymbol());
            tape.writeOnHead(currentTransition.getWriteSymbol());

            //Move head based on found Transition
            Transition.TransitionAction simulationAction = currentTransition.getAction();
            switch (currentTransition.getAction()) {
                case RIGHT:
                    tape.moveHeadRight();
                    stepAction = "RIGHT";
                    break;
                case LEFT:
                    stepAction = "LEFT";
                    tape.moveHeadLeft();
                    break;
                default:
                    stepAction = "STAY";
                    break;
            }


            //modify current state and save simulation step
            stepStateBefore = currentState.name();
            currentState = currentTransition.getNextState();
            stepStateAfter = currentState.name();


            FullSimulationStep currentSimulationStep = new FullSimulationStep(
                    0, simulationAction, String.valueOf(readChar), stepWrittenChar,
                    new State(stepStateBefore), new State(stepStateAfter),tapeState);
            output.steps.get(0).add(currentSimulationStep);
            //decide if program should be stopped
        } while (!currentState.equals(acceptState) && !currentState.equals(rejectState) && maxSteps > curStep);

        return output;
    }
}
