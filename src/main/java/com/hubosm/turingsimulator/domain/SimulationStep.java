package com.hubosm.turingsimulator.domain;

public record SimulationStep(int tapeIndex, Transition.TransitionAction transitionAction, String readChar,
                             String writtenChar, State stateBefore, State stateAfter, TapeState tapeBefore) {

}
