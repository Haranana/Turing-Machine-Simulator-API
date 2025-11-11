package com.hubosm.turingsimulator.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;


public record State(String name) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return Objects.equals(name, state.name);
    }

    @Override
    public String toString() {
        return name;
    }

}
