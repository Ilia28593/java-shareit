package ru.practicum.shareit.ItemRequest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Samples;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ItemRequestImplTest {
    private final ItemRequestService itemRequestService;
    private final UserService userService;
    private final ItemService itemService;
    private final ItemRequestDto itemRequestDto = Samples.getItemRequestDto();
    private UserDto userDto;
    private UserDto userDto2;
    private ItemDto itemDto;

    @BeforeEach
    private void beforeEach() {
        userDto = userService.create(Samples.getUser1());
        userDto2 = userService.create(Samples.getUser2());
        itemDto = itemService.create(Samples.getItem1(), userDto2.getId());
    }

    @Test
    public void create() {
        ItemRequestResponseDto itemRequestResponseDtoSaved =
                itemRequestService.create(itemRequestDto, userDto.getId());
        assertThat(itemRequestResponseDtoSaved)
                .hasFieldOrPropertyWithValue("description", itemRequestDto.getDescription());
    }

    @Test
    public void findByUserId() {
        ItemRequestResponseDto itemRequestResponseDtoSaved =
                itemRequestService.create(itemRequestDto, userDto.getId());
        Collection<ItemRequestResponseDto> itemRequestResponseDtos =
                itemRequestService.findByUserId(userDto.getId());
        assertThat(itemRequestResponseDtos).isEqualTo(List.of(itemRequestResponseDtoSaved));
    }

    @Test
    public void findAllOk() {
        ItemRequestResponseDto itemRequestResponseDtoSaved =
                itemRequestService.create(itemRequestDto, userDto.getId());
        Collection<ItemRequestResponseDto> itemRequestResponseDtos =
                itemRequestService.findAll(0, Integer.MAX_VALUE, userDto2.getId());
        assertThat(itemRequestResponseDtos).isEqualTo(List.of(itemRequestResponseDtoSaved));
    }

    @Test
    public void findAllEmptyResult() {
        ItemRequestResponseDto itemRequestResponseDtoSaved =
                itemRequestService.create(itemRequestDto, userDto.getId());
        Collection<ItemRequestResponseDto> itemRequestResponseDtos =
                itemRequestService.findAll(0, Integer.MAX_VALUE, userDto.getId());
        assertThat(itemRequestResponseDtos).isEqualTo(Collections.emptyList());
    }

    @Test
    public void findByIdWrongIdThrowsError() {
        assertThrows(NotFoundException.class, () -> itemRequestService.findById(-1, userDto.getId()));
    }
}
