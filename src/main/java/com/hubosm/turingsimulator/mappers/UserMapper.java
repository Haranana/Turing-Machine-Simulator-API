package com.hubosm.turingsimulator.mappers;

import com.hubosm.turingsimulator.dtos.UserCreateDto;
import com.hubosm.turingsimulator.dtos.UserReturnDto;
import com.hubosm.turingsimulator.entities.User;
import com.hubosm.turingsimulator.services.SecurityService;
import com.hubosm.turingsimulator.utils.AccountStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class UserMapper {

    final SecurityService securityService;

    public User CreateDtoToEntity(UserCreateDto dto){

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPasswordHash(securityService.encode(dto.getPassword()));
        user.setStatus(AccountStatus.NOT_ACTIVATED);
        user.setTuringMachines(new ArrayList<>());
        user.setActivationToken(securityService.generateSecureToken());
        user.setActivationTokenExpiresAt(OffsetDateTime.now().plusDays(7));
        return user;
    }

    public UserReturnDto EntityToReturnDto(User entity){
        return UserReturnDto.builder().id(entity.getId()).email(entity.getEmail()).status(entity.getStatus()).createdAt(entity.getCreatedAt()).build();
    }
}
