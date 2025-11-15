package com.hubosm.turingsimulator.domain.Nondeterministic;

import com.hubosm.turingsimulator.domain.MultiTransition;
import com.hubosm.turingsimulator.domain.State;
import com.hubosm.turingsimulator.domain.TapeState;

public record NdTmStep (int tapeIndex, Long stepId, MultiTransition.TransitionAction transitionAction, String readChar,
                                 String writtenChar, State stateBefore, State stateAfter, TapeState tapeBefore) {

}