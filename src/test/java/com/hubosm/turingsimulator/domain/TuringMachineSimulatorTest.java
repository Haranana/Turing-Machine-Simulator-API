package com.hubosm.turingsimulator.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class TuringMachineSimulatorTest {
    /*
    //Sample Tm which checks if input starts and ends with the same character in Alphabet = {0,1}
    //Accepts empty input
    TuringMachine getFirstAndLastCharactersEqualityTm(){

        final List<String> program = List.of(
                "qStart,0,0Right,0,R",
                "qStart,1,1Right,1,R",
                "qStart,_,qAcc,_,S",
                "0Right,0,0Right,0,R",
                "0Right,1,0Right,1,R",
                "0Right,_,0Left,_,F",
                "0Left,0,qAcc,0,S",
                "0Left,1,qRej,1,S",
                "0Left,_,qAcc,_,S",
                "1Right,0,1Right,0,R",
                "1Right,1,1Right,1,R",
                "1Right,_,1Left,1,L",
                "1Left,0,qRej,0,S",
                "1Left,1,qAcc,1,S",
                "1Left,_,qAcc,_,S"
        );
        final char sep = ',';
        final String initialState = "qStart";
        final String acceptState = "qAcc";
        final String rejectState = "qRej";

       return new TuringMachine(initialState, acceptState, rejectState, program, sep, "_");
    }

    @Test
    @DisplayName("TuringMachine() on sample program should parse all states")
    void shouldCreateCorrectMachineWhenInitialized() {
        final Set<State> expectedStates = Set.of(new State("qStart"),
                new State("qAcc"),
                new State("qRej"),
                new State("0Right"),
                new State("1Right"),
                new State("0Left"),
                new State("1Left"));

        final State expectedInitialState = new State("qStart");
        final State expectedAcceptState = new State("qAcc");
        final State expectedRejectionState = new State("qRej");

        TuringMachine turingMachine = getFirstAndLastCharactersEqualityTm();

        assertEquals(expectedStates, turingMachine.getStates(), "machine list of states dffers from expected one");
        assertEquals(expectedInitialState, turingMachine.getInitialState(), "machine initial state differs from expected one");
        assertEquals(expectedAcceptState, turingMachine.getAcceptState(), "machine accepting state differs from expected one");
        assertEquals(expectedRejectionState, turingMachine.getRejectState(), "machine rejecting state differs from expected one");


        final List<Transition> expectedProgramTransitions = List.of(
                new Transition(new State("qStart"), '0', new State("0Right"), '0', Transition.TransitionAction.RIGHT),
                new Transition(new State("qStart"), '1', new State("1Right"), '1', Transition.TransitionAction.RIGHT),
                new Transition(new State("qStart"), '_', new State("qAcc"),    '_', Transition.TransitionAction.STAY),

                new Transition(new State("0Right"), '0', new State("0Right"), '0', Transition.TransitionAction.RIGHT),
                new Transition(new State("0Right"), '1', new State("0Right"), '1', Transition.TransitionAction.RIGHT),
                new Transition(new State("0Right"), '_', new State("0Left"),   '_', Transition.TransitionAction.STAY),

                new Transition(new State("0Left"),  '0', new State("qAcc"),    '0', Transition.TransitionAction.STAY),
                new Transition(new State("0Left"),  '1', new State("qRej"),    '1', Transition.TransitionAction.STAY),
                new Transition(new State("0Left"),  '_', new State("qAcc"),    '_', Transition.TransitionAction.STAY),

                new Transition(new State("1Right"), '0', new State("1Right"), '0', Transition.TransitionAction.RIGHT),
                new Transition(new State("1Right"), '1', new State("1Right"), '1', Transition.TransitionAction.RIGHT),
                new Transition(new State("1Right"), '_', new State("1Left"),  '1', Transition.TransitionAction.LEFT),

                new Transition(new State("1Left"),  '0', new State("qRej"),    '0', Transition.TransitionAction.STAY),
                new Transition(new State("1Left"),  '1', new State("qAcc"),    '1', Transition.TransitionAction.STAY),
                new Transition(new State("1Left"),  '_', new State("qAcc"),    '_', Transition.TransitionAction.STAY)

        );

        assertEquals(expectedProgramTransitions , turingMachine.getProgram().getTransitions());
    }

    @Test
    @DisplayName("getTransitionByLeftSide() should correctly return objects when found and exceptions when not" )
    void shouldFindTransitionByLeftSide(){
        TuringMachine turingMachine = getFirstAndLastCharactersEqualityTm();
        assertEquals(
                turingMachine.getProgram().getTransitionByLeftSide(new State("1Left"),'0').get(),
                new Transition(new State("1Left"),  '0', new State("qRej"),    '0', Transition.TransitionAction.STAY));
    }

    @Test
    @DisplayName("run() on simple turing machine should return accept")
    void shouldAcceptWhenInputSameStartEnd(){
        final String input = "101";
        TuringMachine turingMachine = getFirstAndLastCharactersEqualityTm();
        Simulation result = turingMachine.run(input);
        assertTrue(result.getOutput());
    }
*/

}
