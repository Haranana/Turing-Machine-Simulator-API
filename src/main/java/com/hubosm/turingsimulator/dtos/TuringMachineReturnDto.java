package com.hubosm.turingsimulator.dtos;

import lombok.*;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TuringMachineReturnDto {
    private Long id;
    private Long authorId;

    private String name;
    private String description;
    private String program;

    private String initialState;
    private String acceptState;
    private String rejectState;

    private String blank;
    private String sep1;
    private String sep2;

    private Integer tapesAmount;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}