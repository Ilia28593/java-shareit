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
        return new UserResponseDto(source.getId(), source.getName(), source.getEmail());
    }

    public List<UserResponseDto> getListResponse(List<User> list) {
        return list.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
