package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserConvectorResponseDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserController {
    private final UserService userService;
    private final UserConvectorResponseDto userConvectorResponseDto;
    private final UserMapper userMapper;

    @GetMapping
    public List<UserResponseDto> getAllUser() {
        return userConvectorResponseDto.getListResponse(userService.getAll());
    }

    @GetMapping("/{userId}")
    public UserResponseDto get(@PathVariable long userId) {
        return userConvectorResponseDto.convert(userService.getById(userId));
    }

    @PostMapping
    public UserResponseDto create(@RequestBody UserRequestDto requestDto) {
        return userConvectorResponseDto.convert(userService.create(userMapper.mapToUser(requestDto)));
    }

    @PatchMapping("/{userId}")
    public UserResponseDto update(@PathVariable long userId, @RequestBody UserRequestDto requestDto) {
        return userConvectorResponseDto.convert(userService.update(userId, userMapper.mapToUser(requestDto)));
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        userService.delete(userId);
    }
}
