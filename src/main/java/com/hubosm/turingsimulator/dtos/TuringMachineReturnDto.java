package com.hubosm.turingsimulator.dtos;

import com.hubosm.turingsimulator.entities.SpecialSettings;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TuringMachineReturnDto {

    private String name;
    private String description;
    private String program;

    private String initialState;
    private String acceptState;
    private String rejectState;

    private String blank;
    private String sep1;
    private String sep2;
    private String moveRight;
    private String moveLeft;
    private String moveStay;

    private Integer tapesAmount;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    private boolean isPublic;
    private String shareCode;

    private SpecialSettings specialSettings;
}