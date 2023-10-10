package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Samples;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.practicum.shareit.booking.BookingStatus.APPROVED;
import static ru.practicum.shareit.booking.BookingStatusFilter.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
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
        userDto = userService.create(Samples.getUser1());
        userDto2 = userService.create(Samples.getUser2());
        itemDto = itemService.create(Samples.getItem1(), userDto2.getId());
        bookingDto = Samples.getBooking(itemDto.getId());
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
    public void getByIdOk() {
        BookingDtoResponse bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        BookingDtoResponse bookingDtoResponseGetById =
                bookingService.getById(bookingDtoSaved.getId(), userDto.getId());
        assertThat(bookingResponseDtoGetById).isEqualTo(bookingDtoSaved);
    }

    @Test
    public void approveTrue() {
        BookingDtoResponse bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        BookingDtoResponse bookingDtoAfterApproval = bookingService.approve(bookingDtoSaved.getId(), true, userDto2.getId());
        assertThat(bookingDtoAfterApproval).hasFieldOrPropertyWithValue("status", APPROVED);
    }

    @Test
    public void approveFalse() {
        BookingDtoResponse bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        BookingDtoResponse bookingDtoAfterApproval = bookingService.approve(bookingDtoSaved.getId(), false, userDto2.getId());
        assertThat(bookingDtoAfterApproval).hasFieldOrPropertyWithValue("status", BookingStatus.REJECTED);
    }

    @Test
    public void getAllByBooker() {
        BookingDtoResponse bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        Collection<BookingDtoResponse> bookingDtosResponse =
                bookingService.getAllByBookerId(ALL, userDto.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtosResponse).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    public void getCurrentByBooker() {
        bookingDto.setStart(LocalDateTime.now().minusDays(5));
        bookingDto.setEnd(LocalDateTime.now().plusDays(5));
        BookingDtoResponse bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        Collection<BookingDtoResponse> bookingDtosResponse =
                bookingService.getAllByBookerId(CURRENT, userDto.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtosResponse).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    public void getFutureByBooker() {
        bookingDto.setStart(LocalDateTime.now().plusDays(5));
        bookingDto.setEnd(LocalDateTime.now().plusDays(6));
        BookingDtoResponse bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        Collection<BookingDtoResponse> bookingDtosResponse =
                bookingService.getAllByBookerId(FUTURE, userDto.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtosResponse).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    public void getPastByBooker() {
        bookingDto.setStart(LocalDateTime.now().minusDays(5));
        bookingDto.setEnd(LocalDateTime.now().minusDays(4));
        BookingDtoResponse bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        Collection<BookingDtoResponse> bookingDtosResponse =
                bookingService.getAllByBookerId(PAST, userDto.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtosResponse).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    public void getRejectedByBooker() {
        BookingDtoResponse bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        bookingDtoSaved = bookingService.approve(bookingDtoSaved.getId(), false, userDto2.getId());
        Collection<BookingDtoResponse> bookingDtosResponse =
                bookingService.getAllByBookerId(BookingStatusFilter.REJECTED, userDto.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtosResponse).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    public void getWaitingByBooker() {
        BookingDtoResponse bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        Collection<BookingDtoResponse> bookingDtosResponse =
                bookingService.getAllByBookerId(BookingStatusFilter.WAITING, userDto.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtosResponse).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    public void getAllByItemOwner() {
        BookingDtoResponse bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        Collection<BookingDtoResponse> bookingDtosResponse =
                bookingService.getAllByItemOwnerId(ALL, userDto2.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtosResponse).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    public void getCurrentByItemOwner() {
        bookingDto.setStart(LocalDateTime.now().minusDays(5));
        bookingDto.setEnd(LocalDateTime.now().plusDays(5));
        BookingDtoResponse bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        Collection<BookingDtoResponse> bookingDtosResponse =
                bookingService.getAllByItemOwnerId(CURRENT, userDto2.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtosResponse).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    public void getFutureByItemOwner() {
        bookingDto.setStart(LocalDateTime.now().plusDays(5));
        bookingDto.setEnd(LocalDateTime.now().plusDays(6));
        BookingDtoResponse bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        Collection<BookingDtoResponse> bookingDtosResponse =
                bookingService.getAllByItemOwnerId(FUTURE, userDto2.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtosResponse).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    public void getPastByItemOwner() {
        bookingDto.setStart(LocalDateTime.now().minusDays(5));
        bookingDto.setEnd(LocalDateTime.now().minusDays(4));
        BookingDtoResponse bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        Collection<BookingDtoResponse> bookingDtosResponse =
                bookingService.getAllByItemOwnerId(PAST, userDto2.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtosResponse).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    public void getRejectedByItemOwner() {
        BookingDtoResponse bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        bookingDtoSaved = bookingService.approve(bookingDtoSaved.getId(), false, userDto2.getId());
        Collection<BookingDtoResponse> bookingDtosResponse =
                bookingService.getAllByItemOwnerId(BookingStatusFilter.REJECTED, userDto2.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtosResponse).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    public void getWaitingByItemOwner() {
        BookingDtoResponse bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        Collection<BookingDtoResponse> bookingDtosResponse =
                bookingService.getAllByItemOwnerId(BookingStatusFilter.WAITING, userDto2.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtosResponse).isEqualTo(List.of(bookingDtoSaved));
    }
}
