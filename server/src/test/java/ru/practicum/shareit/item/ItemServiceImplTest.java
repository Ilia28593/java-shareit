package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Samples;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
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
    private final ItemDto itemDto = Samples.getItem1();
    private final ItemDto itemDto2 = Samples.getItem2();
    private final CommentDto commentDto = Samples.getComment();
    private UserDto userDto;
    private UserDto userDto2;

    @BeforeEach
    public void beforeEach() {
        userDto = userService.create(Samples.getUser1());
        userDto2 = userService.create(Samples.getUser2());
    }

    @Test
    public void saveResponseIsValid() {
        ItemDto savedItemDto = itemService.create(itemDto, userDto.getId());
        itemDto.setId(savedItemDto.getId());
        assertThat(itemDto).isEqualTo(savedItemDto);
    }

    @Test
    public void saveAndGetByIdAreSame() {
        ItemDto savedItemDto = itemService.create(itemDto, userDto.getId());
        ItemDtoWithBookingDto getByIdItemDtoInBookingDto = itemService.getById(savedItemDto.getId(), userDto.getId());
        assertThat(getByIdItemDtoInBookingDto).isEqualTo(Samples.getItemResponse1(getByIdItemDtoInBookingDto.getId()));
    }

    @Test
    public void updateIsUpdated() {
        ItemDto savedItemDto = itemService.create(itemDto, userDto.getId());
        savedItemDto.setName(savedItemDto.getName() + "test");
        savedItemDto.setDescription(savedItemDto.getDescription() + "test");
        ItemDto itemDtoAfterUpdate = itemService.update(savedItemDto, userDto.getId());
        assertThat(itemDtoAfterUpdate).isEqualTo(savedItemDto);
    }

    @Test
    public void updateThrowsErrorPermissionViolation() {
        ItemDto savedItemDto = itemService.create(itemDto, userDto.getId());
        assertThrows(PermissionViolationException.class,
                () -> itemService.update(savedItemDto, userDto2.getId()));
    }

    @Test
    public void deleteGetByIdRaiseError() {
        ItemDto savedItemDto = itemService.create(itemDto, userDto.getId());
        itemService.delete(savedItemDto.getId());
        assertThrows(NotFoundException.class, () -> itemService.getById(savedItemDto.getId(), userDto.getId()));
    }

    @Test
    public void findByUserIdCorrectResult() {
        ItemDto savedItemDto1 = itemService.create(itemDto, userDto.getId());
        ItemDto savedItemDto2 = itemService.create(itemDto2, userDto2.getId());
        assertThat(itemService.findByUserId(userDto.getId(), 0, Integer.MAX_VALUE))
                .isEqualTo(List.of(Samples.getItemResponse1(savedItemDto1.getId())));
    }

    @Test
    public void searchCorrectResult() {
        ItemDto savedItemDto1 = itemService.create(itemDto, userDto.getId());
        ItemDto savedItemDto2 = itemService.create(itemDto2, userDto2.getId());
        assertThat(itemService.search(savedItemDto1.getName(), userDto.getId(), 0, Integer.MAX_VALUE))
                .isEqualTo(List.of(savedItemDto1));
    }

    @Test
    public void addCommentWithNoBookingThrowBadRequest() {
        ItemDto savedItemDto = itemService.create(itemDto, userDto.getId());
        assertThrows(BadRequestException.class,
                () -> itemService.addComment(commentDto, savedItemDto.getId(), userDto.getId()));
    }

    @Test
    public void addCommentWithNoBookingOk() {
        ItemDto savedItemDto = itemService.create(itemDto, userDto.getId());
        BookingDtoRequest bookingDtoRequest = Samples.getBooking(savedItemDto.getId());
        bookingDtoRequest.setStart(LocalDateTime.now().minusDays(5));
        bookingDtoRequest.setEnd(LocalDateTime.now().minusDays(4));
        BookingDtoResponse bookingDtoResponse =
                bookingService.create(bookingDtoRequest, userDto2.getId());
        bookingService.approve(bookingDtoResponse.getId(), true, userDto.getId());
        assertDoesNotThrow(() -> itemService.addComment(commentDto, savedItemDto.getId(), userDto2.getId()));
    }
}
