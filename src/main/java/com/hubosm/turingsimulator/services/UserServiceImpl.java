package com.hubosm.turingsimulator.services;

import com.hubosm.turingsimulator.dtos.UserCreateDto;
import com.hubosm.turingsimulator.dtos.UserReturnDto;
import com.hubosm.turingsimulator.entities.User;
import com.hubosm.turingsimulator.exceptions.ElementNotFoundException;
import com.hubosm.turingsimulator.mappers.UserMapper;
import com.hubosm.turingsimulator.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public void createUser(UserCreateDto dto) throws Exception {
        User savedUser = userRepository.save(userMapper.CreateDtoToEntity(dto));
        if(!userRepository.existsById(savedUser.getId())) throw new ElementNotFoundException("Problem while saving user has occured");
    }

    @Override
    public UserReturnDto getUser(Long id) throws Exception{
        if(!userRepository.existsById(id)) throw new ElementNotFoundException("User not found");
        User user = userRepository.getReferenceById(id);
        return userMapper.EntityToReturnDto(user);
    }

    @Override
    public void deleteUser(Long id) throws Exception{
        if (!userRepository.existsById(id)) throw new ElementNotFoundException("User not found");
        userRepository.deleteById(id);
    }
}
