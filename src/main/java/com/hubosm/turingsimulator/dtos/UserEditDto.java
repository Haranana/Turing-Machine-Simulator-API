package com.hubosm.turingsimulator.dtos;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEditDto {

    private Long id;

    @Email
    private String email;

    @Size(min = 8, max = 100)
    private String password;

}