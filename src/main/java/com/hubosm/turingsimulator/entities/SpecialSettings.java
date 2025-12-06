package com.hubosm.turingsimulator.entities;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpecialSettings {
    private boolean allowNondeterminism;
    private boolean allowMultipleTapes;
    private boolean onlyComplete;
    private boolean rejectOnNonAccept;

    @NotNull
    private List<String> statesSet;
    private boolean onlyStatesFromSet;

    @NotNull
    private List<String> tapeAlphabet;
    private boolean onlyTapeAlphabet;

    @NotNull
    private List<String> inputAlphabet;
    private boolean onlyInputAlphabet;
}
