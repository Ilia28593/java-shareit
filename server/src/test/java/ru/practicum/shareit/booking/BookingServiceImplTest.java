package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Fixtures;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
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
    private BookingRequestDto bookingDto;
    private ItemDto itemDto;
    private UserDto userDto;
    private UserDto userDto2;

    @BeforeEach
    public void beforeEach() {
        userDto = userService.create(Fixtures.getUser1());
        userDto2 = userService.create(Fixtures.getUser2());
        itemDto = itemService.create(Fixtures.getItem1(), userDto2.getId());
        bookingDto = Fixtures.getBooking(itemDto.getId());
    }

    @Test
    public void bookingServiceImpl_Save_ResponseIsValid() {
        BookingResponseDto bookingResponseDto = bookingService.create(bookingDto, userDto.getId());
        assertThat(bookingResponseDto).hasFieldOrPropertyWithValue("item", itemDto)
                .hasFieldOrPropertyWithValue("booker", userDto)
                .hasFieldOrPropertyWithValue("start", bookingDto.getStart())
                .hasFieldOrPropertyWithValue("end", bookingDto.getEnd())
                .hasFieldOrPropertyWithValue("status", BookingStatus.WAITING);
    }

    @Test
    public void bookingServiceImpl_GetById_Ok() {
        BookingResponseDto bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        BookingResponseDto bookingResponseDtoGetById =
                bookingService.getById(bookingDtoSaved.getId(), userDto.getId());
        assertThat(bookingResponseDtoGetById).isEqualTo(bookingDtoSaved);
    }

    @Test
    public void bookingServiceImpl_Approve_True() {
        BookingResponseDto bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        BookingResponseDto bookingDtoAfterApproval = bookingService.approve(bookingDtoSaved.getId(), true, userDto2.getId());
        assertThat(bookingDtoAfterApproval).hasFieldOrPropertyWithValue("status", APPROVED);
    }

    @Test
    public void bookingServiceImpl_Approve_False() {
        BookingResponseDto bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        BookingResponseDto bookingDtoAfterApproval = bookingService.approve(bookingDtoSaved.getId(), false, userDto2.getId());
        assertThat(bookingDtoAfterApproval).hasFieldOrPropertyWithValue("status", BookingStatus.REJECTED);
    }

    @Test
    public void bookingServiceImpl_GetAllByBooker() {
        BookingResponseDto bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        Collection<BookingResponseDto> bookingDtosResponse =
                bookingService.getAllByBookerId(ALL, userDto.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtosResponse).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    public void bookingServiceImpl_GetCurrentByBooker() {
        bookingDto.setStart(LocalDateTime.now().minusDays(5));
        bookingDto.setEnd(LocalDateTime.now().plusDays(5));
        BookingResponseDto bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        Collection<BookingResponseDto> bookingDtosResponse =
                bookingService.getAllByBookerId(CURRENT, userDto.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtosResponse).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    public void bookingServiceImpl_GetFutureByBooker() {
        bookingDto.setStart(LocalDateTime.now().plusDays(5));
        bookingDto.setEnd(LocalDateTime.now().plusDays(6));
        BookingResponseDto bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        Collection<BookingResponseDto> bookingDtosResponse =
                bookingService.getAllByBookerId(FUTURE, userDto.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtosResponse).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    public void bookingServiceImpl_GetPastByBooker() {
        bookingDto.setStart(LocalDateTime.now().minusDays(5));
        bookingDto.setEnd(LocalDateTime.now().minusDays(4));
        BookingResponseDto bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        Collection<BookingResponseDto> bookingDtosResponse =
                bookingService.getAllByBookerId(PAST, userDto.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtosResponse).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    public void bookingServiceImpl_GetRejectedByBooker() {
        BookingResponseDto bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        bookingDtoSaved = bookingService.approve(bookingDtoSaved.getId(), false, userDto2.getId());
        Collection<BookingResponseDto> bookingDtosResponse =
                bookingService.getAllByBookerId(BookingStatusFilter.REJECTED, userDto.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtosResponse).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    public void bookingServiceImpl_GetWaitingByBooker() {
        BookingResponseDto bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        Collection<BookingResponseDto> bookingDtosResponse =
                bookingService.getAllByBookerId(BookingStatusFilter.WAITING, userDto.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtosResponse).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    public void bookingServiceImpl_GetAllByItemOwner() {
        BookingResponseDto bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        Collection<BookingResponseDto> bookingDtosResponse =
                bookingService.getAllByItemOwnerId(ALL, userDto2.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtosResponse).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    public void bookingServiceImpl_GetCurrentByItemOwner() {
        bookingDto.setStart(LocalDateTime.now().minusDays(5));
        bookingDto.setEnd(LocalDateTime.now().plusDays(5));
        BookingResponseDto bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        Collection<BookingResponseDto> bookingDtosResponse =
                bookingService.getAllByItemOwnerId(CURRENT, userDto2.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtosResponse).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    public void bookingServiceImpl_GetFutureByItemOwner() {
        bookingDto.setStart(LocalDateTime.now().plusDays(5));
        bookingDto.setEnd(LocalDateTime.now().plusDays(6));
        BookingResponseDto bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        Collection<BookingResponseDto> bookingDtosResponse =
                bookingService.getAllByItemOwnerId(FUTURE, userDto2.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtosResponse).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    public void bookingServiceImpl_GetPastByItemOwner() {
        bookingDto.setStart(LocalDateTime.now().minusDays(5));
        bookingDto.setEnd(LocalDateTime.now().minusDays(4));
        BookingResponseDto bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        Collection<BookingResponseDto> bookingDtosResponse =
                bookingService.getAllByItemOwnerId(PAST, userDto2.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtosResponse).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    public void bookingServiceImpl_GetRejectedByItemOwner() {
        BookingResponseDto bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        bookingDtoSaved = bookingService.approve(bookingDtoSaved.getId(), false, userDto2.getId());
        Collection<BookingResponseDto> bookingDtosResponse =
                bookingService.getAllByItemOwnerId(BookingStatusFilter.REJECTED, userDto2.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtosResponse).isEqualTo(List.of(bookingDtoSaved));
    }

    @Test
    public void bookingServiceImpl_GetWaitingByItemOwner() {
        BookingResponseDto bookingDtoSaved = bookingService.create(bookingDto, userDto.getId());
        Collection<BookingResponseDto> bookingDtosResponse =
                bookingService.getAllByItemOwnerId(BookingStatusFilter.WAITING, userDto2.getId(), 0, Integer.MAX_VALUE);
        assertThat(bookingDtosResponse).isEqualTo(List.of(bookingDtoSaved));
    }
}
