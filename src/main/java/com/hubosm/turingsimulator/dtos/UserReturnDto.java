package com.hubosm.turingsimulator.dtos;

import com.hubosm.turingsimulator.utils.AccountStatus;
import lombok.*;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserReturnDto {
    private Long id;
    private String email;
    private AccountStatus status;
    private OffsetDateTime createdAt;
}