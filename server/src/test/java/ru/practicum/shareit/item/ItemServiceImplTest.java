package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Fixtures;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.PermissionViolationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ItemServiceImplTest {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;
    private final ItemDto itemDto = Fixtures.getItem1();
    private final ItemDto itemDto2 = Fixtures.getItem2();
    private final CommentDto commentDto = Fixtures.getComment();
    private UserDto userDto;
    private UserDto userDto2;

    @BeforeEach
    public void beforeEach() {
        userDto = userService.create(Fixtures.getUser1());
        userDto2 = userService.create(Fixtures.getUser2());
    }

    @Test
    public void itemServiceImpl_Save_ResponseIsValid() {
        ItemDto savedItemDto = itemService.create(itemDto, userDto.getId());
        itemDto.setId(savedItemDto.getId());
        assertThat(itemDto).isEqualTo(savedItemDto);
    }

    @Test
    public void itemServiceImpl_SaveAndGetById_AreSame() {
        ItemDto savedItemDto = itemService.create(itemDto, userDto.getId());
        ItemDtoWithBookingDto getByIdItemDtoWithBookingDto = itemService.getById(savedItemDto.getId(), userDto.getId());
        assertThat(getByIdItemDtoWithBookingDto).isEqualTo(Fixtures.getItemResponse1(getByIdItemDtoWithBookingDto.getId()));
    }

    @Test
    public void itemServiceImpl_Update_IsUpdated() {
        ItemDto savedItemDto = itemService.create(itemDto, userDto.getId());
        savedItemDto.setName(savedItemDto.getName() + "test");
        savedItemDto.setDescription(savedItemDto.getDescription() + "test");
        ItemDto itemDtoAfterUpdate = itemService.update(savedItemDto, userDto.getId());
        assertThat(itemDtoAfterUpdate).isEqualTo(savedItemDto);
    }

    @Test
    public void itemServiceImpl_Update_ThrowsErrorPermissionViolation() {
        ItemDto savedItemDto = itemService.create(itemDto, userDto.getId());
        assertThrows(PermissionViolationException.class,
                () -> itemService.update(savedItemDto, userDto2.getId()));
    }

    @Test
    public void itemServiceImpl_Delete_GetByIdRaiseError() {
        ItemDto savedItemDto = itemService.create(itemDto, userDto.getId());
        itemService.delete(savedItemDto.getId());
        assertThrows(NotFoundException.class, () -> itemService.getById(savedItemDto.getId(), userDto.getId()));
    }

    @Test
    public void itemServiceImpl_FindByUserId_CorrectResult() {
        ItemDto savedItemDto1 = itemService.create(itemDto, userDto.getId());
        ItemDto savedItemDto2 = itemService.create(itemDto2, userDto2.getId());
        assertThat(itemService.findByUserId(userDto.getId(), 0, Integer.MAX_VALUE))
                .isEqualTo(List.of(Fixtures.getItemResponse1(savedItemDto1.getId())));
    }

    @Test
    public void itemServiceImpl_Search_CorrectResult() {
        ItemDto savedItemDto1 = itemService.create(itemDto, userDto.getId());
        ItemDto savedItemDto2 = itemService.create(itemDto2, userDto2.getId());
        assertThat(itemService.search(savedItemDto1.getName(), userDto.getId(), 0, Integer.MAX_VALUE))
                .isEqualTo(List.of(savedItemDto1));
    }

    @Test
    public void itemServiceImpl_AddCommentWithNoBooking_ThrowBadRequest() {
        ItemDto savedItemDto = itemService.create(itemDto, userDto.getId());
        assertThrows(BadRequestException.class,
                () -> itemService.addComment(commentDto, savedItemDto.getId(), userDto.getId()));
    }

    @Test
    public void itemServiceImpl_AddCommentWithNoBooking_Ok() {
        ItemDto savedItemDto = itemService.create(itemDto, userDto.getId());
        BookingRequestDto bookingRequestDto = Fixtures.getBooking(savedItemDto.getId());
        bookingRequestDto.setStart(LocalDateTime.now().minusDays(5));
        bookingRequestDto.setEnd(LocalDateTime.now().minusDays(4));
        BookingResponseDto bookingResponseDto =
                bookingService.create(bookingRequestDto, userDto2.getId());
        bookingService.approve(bookingResponseDto.getId(), true, userDto.getId());
        assertDoesNotThrow(() -> itemService.addComment(commentDto, savedItemDto.getId(), userDto2.getId()));
    }
}
