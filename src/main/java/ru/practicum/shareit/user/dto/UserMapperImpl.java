package ru.practicum.shareit.user.dto;


import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import javax.annotation.processing.Generated;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2023-06-14T20:27:59+0300",
        comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper{
    @Override
    public User mapToUser(UserRequestDto dto) {
        if (dto == null) {
            return null;
        }
        return new User(dto.getName(), dto.getEmail());
    }
}
