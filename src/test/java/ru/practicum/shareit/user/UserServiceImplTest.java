package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Fixtures;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserServiceImplTest {
    private final UserService userService;

    @Test
    void userServiceImpl_Save_ResponseIsValid() {
        UserDto userDto = Fixtures.getUser_1();
        UserDto savedUserDto = userService.create(userDto);
        userDto.setId(savedUserDto.getId());
        assertThat(userDto).isEqualTo(savedUserDto);
    }

    @Test
    void userServiceImpl_SaveAndGetById_AreSame() {
        UserDto userDto = Fixtures.getUser_1();
        UserDto savedUserDto = userService.create(userDto);
        UserDto getByIdUserDto = userService.getById(savedUserDto.getId());
        assertThat(savedUserDto).isEqualTo(getByIdUserDto);
    }

    @Test
    void userServiceImpl_Update_IsUpdated() {
        UserDto userDtoBeforeUpdate = Fixtures.getUser_1();
        UserDto savedUserDtoBeforeUpdate = userService.create(userDtoBeforeUpdate);
        UserDto updatedUserDto = Fixtures.getUser_2();
        updatedUserDto.setId(savedUserDtoBeforeUpdate.getId());
        UserDto savedUserDtoAfterUpdate = userService.update(updatedUserDto);
        UserDto getByIdUserDto = userService.getById(savedUserDtoAfterUpdate.getId());
        assertAll(() -> assertThat(getByIdUserDto).isNotEqualTo(savedUserDtoBeforeUpdate),
                () -> assertThat(getByIdUserDto).isEqualTo(savedUserDtoAfterUpdate));
    }

    @Test
    void userServiceImpl_Delete_GetByIdRaiseError() {
        UserDto userDto = Fixtures.getUser_1();
        UserDto savedUserDto = userService.create(userDto);
        userService.delete(savedUserDto.getId());
        assertThrows(NotFoundException.class, () -> userService.getById(savedUserDto.getId()));
    }

    @Test
    void userServiceImpl_GetAll_TwoUsersReturned() {
        UserDto savedUserDto1 = userService.create(Fixtures.getUser_1());
        UserDto savedUserDto2 = userService.create(Fixtures.getUser_2());
        assertThat(userService.getAll()).isEqualTo(List.of(savedUserDto1, savedUserDto2));
    }
}
