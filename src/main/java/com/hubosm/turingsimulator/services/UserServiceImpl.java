package com.hubosm.turingsimulator.services;

import com.hubosm.turingsimulator.dtos.UserCreateDto;
import com.hubosm.turingsimulator.dtos.UserReturnDto;
import com.hubosm.turingsimulator.entities.User;
import com.hubosm.turingsimulator.exceptions.AccountActivationException;
import com.hubosm.turingsimulator.exceptions.AccountNotActiveException;
import com.hubosm.turingsimulator.exceptions.ElementNotFoundException;
import com.hubosm.turingsimulator.exceptions.ExpirationDatePassedException;
import com.hubosm.turingsimulator.mappers.UserMapper;
import com.hubosm.turingsimulator.repositories.UserRepository;
import com.hubosm.turingsimulator.utils.AccountStatus;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;


@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmailServiceImpl emailService;
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @Override
    public void createUser(UserCreateDto dto) throws Exception {
        User savedUser = userRepository.save(userMapper.CreateDtoToEntity(dto));
        if(!userRepository.existsById(savedUser.getId())) throw new ElementNotFoundException("Problem while saving user has occured");

        emailService.sendActivationMail(savedUser.getEmail(), savedUser.getActivationToken(), savedUser.getActivationTokenExpiresAt().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
    }

    @Override
    public UserReturnDto getUser(Long id) throws Exception{
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("User not found"));
        return userMapper.EntityToReturnDto(user);
    }

    @Override
    public void deleteUser(Long id) throws Exception{
        if (!userRepository.existsById(id)) throw new ElementNotFoundException("User not found");
        userRepository.deleteById(id);
    }

    @Override
    public void activateUserAccount(String activationToken) throws Exception {
        User user = userRepository.findByActivationToken(activationToken)
                .orElseThrow(() -> new ElementNotFoundException("Invalid activation token"));

        if (user.isEnabled()) {
            throw new AccountActivationException("User account is already active");
        }
        if (user.getActivationToken() == null || user.getActivationTokenExpiresAt() == null) {
            throw new AccountActivationException("Activation token doesn't exist");
        }
        if (OffsetDateTime.now().isAfter(user.getActivationTokenExpiresAt())) {
            throw new AccountActivationException("Activation link expired");
        }

        user.setStatus(AccountStatus.ACTIVE);
        user.setActivationToken(null);
        user.setActivationTokenExpiresAt(null);
        userRepository.save(user);
    }
}
