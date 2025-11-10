package com.hubosm.turingsimulator.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TuringMachineCreateDto {

    @NotBlank
    @Size(max = 30)
    private String name;

    @Size(max = 255)
    private String description;

    private String program;

    @NotBlank
    @Size(max = 32)
    private String initialState;

    @Size(max = 32)
    private String acceptState;

    @Size(max = 32)
    private String rejectState;

    @NotBlank
    @Size(max = 10)
    private String blank;

    @NotBlank
    @Size(max = 10)
    private String sep1;

    @NotBlank
    @Size(max = 10)
    private String sep2;

    @NotBlank
    @Size(max = 10)
    private String moveRight;

    @NotBlank
    @Size(max = 10)
    private String moveLeft;

    @NotBlank
    @Size(max = 10)
    private String moveStay;

    @NotNull
    @Min(1)
    private Integer tapesAmount;
}