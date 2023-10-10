package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Samples;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserServiceImplTest {
    private final UserService userService;

    @Test
    public void saveResponseIsValid() {
        UserDto userDto = Samples.getUser1();
        UserDto savedUserDto = userService.create(userDto);
        userDto.setId(savedUserDto.getId());
        assertThat(userDto).isEqualTo(savedUserDto);
    }

    @Test
    public void saveAndGetByIdAreSame() {
        UserDto userDto = Samples.getUser1();
        UserDto savedUserDto = userService.create(userDto);
        UserDto getByIdUserDto = userService.getById(savedUserDto.getId());
        assertThat(savedUserDto).isEqualTo(getByIdUserDto);
    }

    @Test
    public void updateIsUpdated() {
        UserDto userDtoBeforeUpdate = Samples.getUser1();
        UserDto savedUserDtoBeforeUpdate = userService.create(userDtoBeforeUpdate);
        UserDto updatedUserDto = Samples.getUser2();
        updatedUserDto.setId(savedUserDtoBeforeUpdate.getId());
        UserDto savedUserDtoAfterUpdate = userService.update(updatedUserDto);
        UserDto getByIdUserDto = userService.getById(savedUserDtoAfterUpdate.getId());
        assertAll(() -> assertThat(getByIdUserDto).isNotEqualTo(savedUserDtoBeforeUpdate),
                () -> assertThat(getByIdUserDto).isEqualTo(savedUserDtoAfterUpdate));
    }

    @Test
    public void deleteGetByIdRaiseError() {
        UserDto userDto = Samples.getUser1();
        UserDto savedUserDto = userService.create(userDto);
        userService.delete(savedUserDto.getId());
        assertThrows(NotFoundException.class, () -> userService.getById(savedUserDto.getId()));
    }

    @Test
    public void getAllTwoUsersReturned() {
        UserDto savedUserDto1 = userService.create(Samples.getUser1());
        UserDto savedUserDto2 = userService.create(Samples.getUser2());
        assertThat(userService.getAll()).isEqualTo(List.of(savedUserDto1, savedUserDto2));
    }
}
