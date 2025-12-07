package com.hubosm.turingsimulator.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Collection;
import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class SimulationCreateDto {
    @NotBlank private String initialState;
    @NotBlank private String acceptState;
    private String rejectState;

    @NotNull  private Collection<String> program;
    @NotBlank private String sep1;
    @NotBlank private String sep2;
    @NotBlank private String blank;

    @NotNull  private List<String> input;
    @NotNull  private Integer tapesAmount;
}