package com.hubosm.turingsimulator.domain;

public record FullSimulationStep(int tapeIndex, Transition.TransitionAction transitionAction, String readChar,
                                String writtenChar, State stateBefore, State stateAfter, TapeState tapeBefore) {

}
