package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

import static ru.practicum.shareit.config.Constants.USER_NO_FOUND_FROM_ID;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto getById(long userId) {
        return userMapper.toUserDto(findById(userId));
    }

    @Transactional
    @Override
    public UserDto create(UserDto userDto) {
        return userMapper.toUserDto(userRepository.save(userMapper.toUser(userDto)));
    }

    @Transactional
    @Override
    public UserDto update(UserDto userDto) {
        User user = findById(userDto.getId());
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Transactional
    @Override
    public void delete(long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public Collection<UserDto> getAll() {
        return userRepository.findAll()
                .stream().map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public User findById(long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NO_FOUND_FROM_ID + userId));
    }
}
