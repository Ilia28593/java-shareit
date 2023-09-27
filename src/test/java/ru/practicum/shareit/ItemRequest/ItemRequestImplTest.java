package ru.practicum.shareit.ItemRequest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Fixtures;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

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
    private final ItemRequestDto itemRequestDto = Fixtures.getItemRequestDto();
    private UserDto userDto;
    private UserDto userDto2;
    private ItemDto itemDto;

    @BeforeEach
    private void beforeEach() {
        userDto = userService.create(Fixtures.getUser1());
        userDto2 = userService.create(Fixtures.getUser2());
        itemDto = itemService.create(Fixtures.getItem1(), userDto2.getId());
    }

    @Test
    public void itemRequestImpl_Create() {
        ItemRequestResponseDto itemRequestResponseDtoSaved =
                itemRequestService.create(itemRequestDto, userDto.getId());
        assertThat(itemRequestResponseDtoSaved)
                .hasFieldOrPropertyWithValue("description", itemRequestDto.getDescription());
    }

    @Test
    public void itemRequestImpl_FindByUserId() {
        ItemRequestResponseDto itemRequestResponseDtoSaved =
                itemRequestService.create(itemRequestDto, userDto.getId());
        Collection<ItemRequestResponseDto> itemRequestResponseDtos =
                itemRequestService.findByUserId(userDto.getId());
        assertThat(itemRequestResponseDtos).isEqualTo(List.of(itemRequestResponseDtoSaved));
    }

    @Test
    public void itemRequestImpl_FindAll_Ok() {
        ItemRequestResponseDto itemRequestResponseDtoSaved =
                itemRequestService.create(itemRequestDto, userDto.getId());
        Collection<ItemRequestResponseDto> itemRequestResponseDtos =
                itemRequestService.findAll(0, Integer.MAX_VALUE, userDto2.getId());
        assertThat(itemRequestResponseDtos).isEqualTo(List.of(itemRequestResponseDtoSaved));
    }

    @Test
    public void itemRequestImpl_FindAll_EmptyResult() {
        ItemRequestResponseDto itemRequestResponseDtoSaved =
                itemRequestService.create(itemRequestDto, userDto.getId());
        Collection<ItemRequestResponseDto> itemRequestResponseDtos =
                itemRequestService.findAll(0, Integer.MAX_VALUE, userDto.getId());
        assertThat(itemRequestResponseDtos).isEqualTo(Collections.emptyList());
    }

    @Test
    public void itemRequestImpl_FindById_Ok() {
        ItemRequestResponseDto itemRequestResponseDtoSaved =
                itemRequestService.create(itemRequestDto, userDto.getId());
        ItemRequestResponseDto itemRequestResponseDtoById =
                itemRequestService.findById(itemRequestResponseDtoSaved.getId(), userDto.getId());
        assertThat(itemRequestResponseDtoById).isEqualTo(itemRequestResponseDtoSaved);
    }

    @Test
    public void itemRequestImpl_FindById_WrongIdThrowsError() {
        assertThrows(NotFoundException.class, () -> itemRequestService.findById(-1, userDto.getId()));
    }
}
