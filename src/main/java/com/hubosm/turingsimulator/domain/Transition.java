package com.hubosm.turingsimulator.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Setter
@Getter
@AllArgsConstructor
public class Transition {
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

    @Override
    public String toString() {
        return "[" + currentState +
                ", " + Arrays.toString(read) +
                " => " + nextState +
                ", " + Arrays.toString(write) +
                ", " + Arrays.toString(actions) +
                ']';
    }
}
