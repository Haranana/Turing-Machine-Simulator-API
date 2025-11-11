package com.hubosm.turingsimulator.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class MultiTransition {
    private State currentState;
    private String[] read;
    private State nextState;
    private String[] write;
    private TransitionAction[] actions;

    public static TransitionAction stringToAction(String action){
        if(action.equals("RIGHT")){
            return TransitionAction.RIGHT;
        }else if(action.equals("LEFT")){
            return TransitionAction.LEFT;
        }else {
            return TransitionAction.STAY;
        }
    }

    public enum TransitionAction{
        RIGHT,
        LEFT,
        STAY
    }
}
