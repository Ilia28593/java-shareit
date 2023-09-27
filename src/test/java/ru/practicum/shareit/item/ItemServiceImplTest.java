package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Fixtures;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.PermissionViolationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoInBookingDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest()
public class ItemServiceImplTest {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;
    private final ItemDto itemDto = Fixtures.getItem_1();
    private final ItemDto itemDto2 = Fixtures.getItem_2();
    private final CommentDto commentDto = Fixtures.getComment_1();
    private UserDto userDto;
    private UserDto userDto2;

    @BeforeEach
    public void beforeEach() {
        userDto = userService.create(Fixtures.getUser_1());
        userDto2 = userService.create(Fixtures.getUser_2());
    }

    @Test
    void save() {
        ItemDto savedItemDto = itemService.create(itemDto, userDto.getId());
        itemDto.setId(savedItemDto.getId());
        assertThat(itemDto).isEqualTo(savedItemDto);
    }

    @Test
    void saveAndGetById() {
        ItemDto savedItemDto = itemService.create(itemDto, userDto.getId());
        ItemDtoInBookingDto getByIdItemDtoInBookingDto = itemService.getById(savedItemDto.getId(), userDto.getId());
        assertThat(getByIdItemDtoInBookingDto).isEqualTo(Fixtures.getResponseItem_1(getByIdItemDtoInBookingDto.getId()));
    }

    @Test
    void update() {
        ItemDto savedItemDto = itemService.create(itemDto, userDto.getId());
        savedItemDto.setName(savedItemDto.getName() + "test");
        savedItemDto.setDescription(savedItemDto.getDescription() + "test");
        ItemDto itemDtoAfterUpdate = itemService.update(savedItemDto, userDto.getId());
        assertThat(itemDtoAfterUpdate).isEqualTo(savedItemDto);
    }

    @Test
    void updateThrows() {
        ItemDto savedItemDto = itemService.create(itemDto, userDto.getId());
        assertThrows(PermissionViolationException.class,
                () -> itemService.update(savedItemDto, userDto2.getId()));
    }

    @Test
    void delete() {
        ItemDto savedItemDto = itemService.create(itemDto, userDto.getId());
        itemService.delete(savedItemDto.getId());
        assertThrows(NotFoundException.class, () -> itemService.getById(savedItemDto.getId(), userDto.getId()));
    }

    @Test
    void findByUserId() {
        ItemDto savedItemDto1 = itemService.create(itemDto, userDto.getId());
        ItemDto savedItemDto2 = itemService.create(itemDto2, userDto2.getId());
        assertThat(itemService.findByUserId(userDto.getId(), 0, Integer.MAX_VALUE))
                .isEqualTo(List.of(Fixtures.getResponseItem_1(savedItemDto1.getId())));
    }

    @Test
    void search() {
        ItemDto savedItemDto1 = itemService.create(itemDto, userDto.getId());
        ItemDto savedItemDto2 = itemService.create(itemDto2, userDto2.getId());
        assertThat(itemService.search(savedItemDto1.getName(), userDto.getId(), 0, Integer.MAX_VALUE))
                .isEqualTo(List.of(savedItemDto1));
    }

    @Test
    void addCommentWithNoBooking() {
        ItemDto savedItemDto = itemService.create(itemDto, userDto.getId());
        assertThrows(BadRequestException.class,
                () -> itemService.addComment(commentDto, savedItemDto.getId(), userDto.getId()));
    }

    @Test
    void addCommentWithNoBookingOk() {
        ItemDto savedItemDto = itemService.create(itemDto, userDto.getId());
        BookingDtoRequest bookingDtoRequest = Fixtures.getBooking(savedItemDto.getId())
                .setStart(LocalDateTime.now().minusDays(5))
                .setEnd(LocalDateTime.now().minusDays(4));
        BookingDtoResponse bookingDtoResponse = bookingService.create(bookingDtoRequest, userDto2.getId());
        bookingService.approved(bookingDtoResponse.getId(), true, userDto.getId());
        assertDoesNotThrow(() -> itemService.addComment(commentDto, savedItemDto.getId(), userDto2.getId()));
    }
}
