package ru.practicum.shareit.user.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    @Override
    public User toUser(UserDto userDto) {
        return new User()
                .setId(userDto.getId())
                .setName(userDto.getName())
                .setEmail(userDto.getEmail());
    }
}
