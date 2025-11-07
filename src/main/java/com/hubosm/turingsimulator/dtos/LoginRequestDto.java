package com.hubosm.turingsimulator.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDto (@Email @NotBlank String email, @NotBlank String password){
}
