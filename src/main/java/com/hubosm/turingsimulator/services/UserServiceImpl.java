package com.hubosm.turingsimulator.services;

import com.hubosm.turingsimulator.dtos.UserChangePasswordDto;
import com.hubosm.turingsimulator.dtos.UserCreateDto;
import com.hubosm.turingsimulator.dtos.UserReturnDto;
import com.hubosm.turingsimulator.entities.User;
import com.hubosm.turingsimulator.exceptions.*;
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
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmailServiceImpl emailService;
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private final SecurityServiceImpl securityService;

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

    @Override
    public void changePassword(UserChangePasswordDto dto, String token) throws Exception{
        User user = userRepository.findByPasswordChangeToken(token).orElseThrow(()->new ElementNotFoundException("User not found"));

        if(user.getPasswordChangeToken() == null || user.getPasswordChangeTokenExpiresAt() == null){
            throw  new PasswordChangeException("Token doesn't exist");
        }
        if(user.getPasswordChangeTokenExpiresAt().isBefore(OffsetDateTime.now())){
            throw new PasswordChangeException("Token expired");
        }

        user.setPasswordHash(securityService.encode(dto.getPassword()));
        user.setPasswordChangeToken(null);
        user.setPasswordChangeTokenExpiresAt(null);
        userRepository.save(user);

    }

    @Override
    public void addChangePasswordToken(String userEmail) throws Exception{
        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        if(userOptional.isEmpty()){
            return;
        }

        User user = userOptional.get();
        String token = securityService.generateSecureToken();
        OffsetDateTime expirationDate = OffsetDateTime.now().plusMinutes(30);

        emailService.sendPasswordChangeMail(userEmail, token, expirationDate.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
        user.setPasswordChangeToken(token);
        user.setPasswordChangeTokenExpiresAt(expirationDate);
        userRepository.save(user);
    }

    @Override
    public void addDeleteAccountToken(Long userId) throws Exception{
        User user = userRepository.findById(userId).orElseThrow(()->new ElementNotFoundException("User not found"));

        String token = securityService.generateSecureToken();
        OffsetDateTime expirationDate = OffsetDateTime.now().plusMinutes(30);

        emailService.sendDeleteAccountMail(user.getEmail(), token, expirationDate.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
        user.setDeleteAccountToken(token);
        user.setDeleteAccountTokenExpiresAt(expirationDate);
        userRepository.save(user);
    }

    @Override
    public void deleteAccount(String token) throws Exception{
        User user = userRepository.findByDeleteAccountToken(token).orElseThrow(()->new ElementNotFoundException("Invalid token"));

        if(user.getDeleteAccountTokenExpiresAt() == null){
            throw new DeleteAccountException("Invalid token");
        }
        if(user.getDeleteAccountTokenExpiresAt().isBefore(OffsetDateTime.now())){
            throw new DeleteAccountException("Token expired");
        }

        userRepository.delete(user);
    }
}
