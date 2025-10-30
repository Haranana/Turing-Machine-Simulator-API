package com.hubosm.turingsimulator.dtos;

public record SimulationStepDto(
        int tapeIndex,
        String action, //LEFT,RIGHT,STAY
        String readChar,
        String writtenChar,
        String stateBefore,
        String stateAfter
) {}
