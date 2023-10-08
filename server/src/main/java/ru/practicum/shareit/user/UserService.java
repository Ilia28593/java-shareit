package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

@Service
public interface UserService {
    UserDto getById(long userId);

    UserDto create(UserDto userDto);

    UserDto update(UserDto userDto);

    void delete(long userId);

    Collection<UserDto> getAll();
}
