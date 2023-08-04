package ru.practicum.shareit.user.dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConvectorResponseDto implements Converter<User, UserResponseDto> {
    @Override
    public UserResponseDto convert(User source) {
        return UserResponseDto.builder()
                .id(source.getId())
                .name(source.getName())
                .email(source.getEmail())
                .build();
    }

    public List<UserResponseDto> getListResponse(List<User> list) {
        return list.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
