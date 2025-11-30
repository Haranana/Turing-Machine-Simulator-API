package com.hubosm.turingsimulator.dtos;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserChangePasswordDto {
    @NotBlank
    @Size(min = 8, max = 100)
    private String password;
}