package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Fixtures;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.practicum.shareit.booking.model.BookingStatus.APPROVED;
import static ru.practicum.shareit.booking.model.BookingStatusFilter.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class BookingServiceImplTest {
    private final BookingService bookingService;
    private final ItemService itemService;
    private final UserService userService;
    private BookingDtoRequest bookingDto;
    private ItemDto itemDto;
    private UserDto userDto;
    private UserDto userDto2;

    @BeforeEach
    public void beforeEach() {
        userDto = userService.create(Fixtures.getUser_1());
        userDto2 = userService.create(Fixtures.getUser_2());
        itemDto = itemService.create(Fixtures.getItem_1(), userDto2.getId());
        bookingDto = Fixtures.getBooking(itemDto.getId());
    }

    @Test
    public void saveResponseIsValid() {
        BookingDtoResponse bookingDtoResponse = bookingService.create(bookingDto, userDto.getId());
        assertThat(bookingDtoResponse).hasFieldOrPropertyWithValue("item", itemDto)
                .hasFieldOrPropertyWithValue("booker", userDto)
                .hasFieldOrPropertyWithValue("start", bookingDto.getStart())
                .hasFieldOrPropertyWithValue("end", bookingDto.getEnd())
                .hasFieldOrPropertyWithValue("status", BookingStatus.WAITING);
    }

    @Test
    void getById_Ok() {
        BookingDtoResponse bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        BookingDtoResponse bookingDtoResponseGetById =
                bookingService.getById(bookingDtoSaved.getId(), userDto.getId());
        assertThat(bookingDtoResponseGetById).isEqualTo(bookingDtoSaved);
    }

    @Test
    void approve_True() {
        BookingDtoResponse bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        BookingDtoResponse bookingDtoAfterApproval = bookingService.approved(bookingDtoSaved.getId(), true, userDto2.getId());
        assertThat(bookingDtoAfterApproval).hasFieldOrPropertyWithValue("status", APPROVED);
    }

    @Test
    void approve_False() {
        BookingDtoResponse bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        BookingDtoResponse bookingDtoAfterApproval = bookingService.approved(bookingDtoSaved.getId(), false, userDto2.getId());
        assertThat(bookingDtoAfterApproval).hasFieldOrPropertyWithValue("status", BookingStatus.REJECTED);
    }

    @Test
    void getAllByBooker() {
        BookingDtoResponse bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        Collection<BookingDtoResponse> bookingDtosResponse =
                bookingService.getAllByBookerId(ALL, userDto.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtosResponse).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    void getCurrentByBooker() {
        bookingDto.setStart(LocalDateTime.now().minusDays(5));
        bookingDto.setEnd(LocalDateTime.now().plusDays(5));
        BookingDtoResponse bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        Collection<BookingDtoResponse> bookingDtosResponse =
                bookingService.getAllByBookerId(CURRENT, userDto.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtosResponse).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    void getFutureByBooker() {
        bookingDto.setStart(LocalDateTime.now().plusDays(5));
        bookingDto.setEnd(LocalDateTime.now().plusDays(6));
        BookingDtoResponse bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        Collection<BookingDtoResponse> bookingDtosResponse =
                bookingService.getAllByBookerId(FUTURE, userDto.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtosResponse).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    void getPastByBooker() {
        bookingDto.setStart(LocalDateTime.now().minusDays(5));
        bookingDto.setEnd(LocalDateTime.now().minusDays(4));
        BookingDtoResponse bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        Collection<BookingDtoResponse> bookingDtoResponseCollection =
                bookingService.getAllByBookerId(PAST, userDto.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtoResponseCollection).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    void getRejectedByBooker() {
        BookingDtoResponse bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        bookingDtoSaved = bookingService.approved(bookingDtoSaved.getId(), false, userDto2.getId());
        Collection<BookingDtoResponse> bookingDtoResponseCollection =
                bookingService.getAllByBookerId(REJECTED, userDto.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtoResponseCollection).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    void getWaitingByBooker() {
        BookingDtoResponse bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        Collection<BookingDtoResponse> bookingDtoResponseCollection =
                bookingService.getAllByBookerId(WAITING, userDto.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtoResponseCollection).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    void getAllByItemOwner() {
        BookingDtoResponse bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        Collection<BookingDtoResponse> bookingDtoResponseCollection =
                bookingService.getAllByItemOwnerId(ALL, userDto2.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtoResponseCollection).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    public void bookingServiceImpl_GetCurrentByItemOwner() {
        bookingDto.setStart(LocalDateTime.now().minusDays(5));
        bookingDto.setEnd(LocalDateTime.now().plusDays(5));
        BookingDtoResponse bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        Collection<BookingDtoResponse> bookingDtoResponseCollection =
                bookingService.getAllByItemOwnerId(CURRENT, userDto2.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtoResponseCollection).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    void getFutureByItemOwner() {
        bookingDto.setStart(LocalDateTime.now().plusDays(5));
        bookingDto.setEnd(LocalDateTime.now().plusDays(6));
        BookingDtoResponse bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        Collection<BookingDtoResponse> bookingDtoResponseCollection =
                bookingService.getAllByItemOwnerId(FUTURE, userDto2.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtoResponseCollection).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    void getPastByItemOwner() {
        bookingDto.setStart(LocalDateTime.now().minusDays(5));
        bookingDto.setEnd(LocalDateTime.now().minusDays(4));
        BookingDtoResponse bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        Collection<BookingDtoResponse> bookingDtoResponseCollection =
                bookingService.getAllByItemOwnerId(PAST, userDto2.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtoResponseCollection).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    void getRejectedByItemOwner() {
        BookingDtoResponse bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        bookingDtoSaved = bookingService.approved(bookingDtoSaved.getId(), false, userDto2.getId());
        Collection<BookingDtoResponse> bookingDtoResponseCollection =
                bookingService.getAllByItemOwnerId(REJECTED, userDto2.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtoResponseCollection).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    void getWaitingByItemOwner() {
        BookingDtoResponse bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        Collection<BookingDtoResponse> bookingDtoResponseCollection =
                bookingService.getAllByItemOwnerId(WAITING, userDto2.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtoResponseCollection).isEqualTo(List.of(bookingDtoSaved));
    }
}
