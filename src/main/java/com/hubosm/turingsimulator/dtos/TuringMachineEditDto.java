package com.hubosm.turingsimulator.dtos;

import com.hubosm.turingsimulator.entities.SpecialSettings;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TuringMachineEditDto {

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

    @Size(max = 10)
    private String moveRight;

    @Size(max = 10)
    private String moveLeft;

    @Size(max = 10)
    private String moveStay;

    @Min(1)
    private Integer tapesAmount;

    @Valid
    private SpecialSettings specialSettings;
}