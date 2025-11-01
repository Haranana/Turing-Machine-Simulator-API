package com.hubosm.turingsimulator.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Collection;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreateSimulationDto {
    @NotBlank
    private String initialState;
    @NotBlank
    private String acceptState;
    @NotBlank
    private String rejectState;
    private Collection<String> program;
    @NotNull
    private Character separator;
    @NotNull
    private String blank;
    private String input;
}
