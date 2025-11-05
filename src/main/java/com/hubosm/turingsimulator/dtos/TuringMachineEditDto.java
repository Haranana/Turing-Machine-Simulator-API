package com.hubosm.turingsimulator.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TuringMachineEditDto {

    private Long id;

    @Size(max = 30)
    private String name;

    @Size(max = 255)
    private String description;

    private String program;

    @Size(max = 32)
    private String initialState;

    @Size(max = 32)
    private String acceptState;

    @Size(max = 32)
    private String rejectState;

    @Size(max = 10)
    private String blank;

    @Size(max = 10)
    private String sep1;

    @Size(max = 10)
    private String sep2;

    @Min(1)
    private Integer tapesAmount;
}