package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

@Service
public interface UserService {
    UserDto getById(long userId);

    UserDto create(UserDto userDto);

    UserDto update(UserDto userDto);

    void delete(long userId);

    Collection<UserDto> getAll();

    User getUserById(long userId);
}
