package ru.practicum.shareit.user.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

@Component
public interface UserMapper {
    UserDto toUserDto(User user);

    User toUser(UserDto userDto);
}
