package com.hubosm.turingsimulator.services;

import com.hubosm.turingsimulator.dtos.UserChangePasswordDto;
import com.hubosm.turingsimulator.dtos.UserCreateDto;
import com.hubosm.turingsimulator.dtos.UserReturnDto;

public interface UserService {
     void createUser(UserCreateDto dto) throws Exception;
     UserReturnDto getUser(Long id) throws Exception;
    void deleteUser(Long id) throws Exception;
    void activateUserAccount(String activationToken) throws Exception;
    void changePassword(UserChangePasswordDto dto, String token) throws Exception;
    void addChangePasswordToken(String userEmail) throws Exception;
    void addDeleteAccountToken(Long userId) throws Exception;
    void deleteAccount(String token) throws Exception;
}
