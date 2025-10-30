package com.hubosm.turingsimulator.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
public class Transition {

    private State currentState;
    private char readSymbol;
    private State nextState;
    private char writeSymbol;
    private TransitionAction action;

    public static TransitionAction stringToAction(String action){
        if(action.equals("RIGHT")){
            return TransitionAction.RIGHT;
        }else if(action.equals("LEFT")){
            return TransitionAction.LEFT;
        }else {
            return TransitionAction.STAY;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transition that = (Transition) o;
        return readSymbol == that.readSymbol && writeSymbol == that.writeSymbol && Objects.equals(currentState, that.currentState) && Objects.equals(nextState, that.nextState) && action == that.action;
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentState, readSymbol, nextState, writeSymbol, action);
    }

    @Override
    public String toString() {
        return "Transition{" +
                currentState +
                " ; " + readSymbol +
                " -> " + nextState +
                " ; " + writeSymbol +
                " ; " + action +
                '}';
    }

    public enum TransitionAction{
        RIGHT,
        LEFT,
        STAY
    }
}
