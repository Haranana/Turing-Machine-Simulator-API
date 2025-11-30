package com.hubosm.turingsimulator.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserPasswordChangeRequestDto {
    @Email
    @NotBlank
    private String email;
}
