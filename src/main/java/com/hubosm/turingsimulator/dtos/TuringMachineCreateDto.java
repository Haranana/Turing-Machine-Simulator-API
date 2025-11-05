package com.hubosm.turingsimulator.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TuringMachineCreateDto {

    @NotNull
    private Long authorId;

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

    @NotNull
    @Min(1)
    private Integer tapesAmount;
}