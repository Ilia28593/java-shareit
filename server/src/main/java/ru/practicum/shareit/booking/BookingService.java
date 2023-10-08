package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.Collection;

public interface BookingService {
    BookingResponseDto create(BookingRequestDto bookingRequestDto, long userId);

    BookingResponseDto approve(long bookingId, boolean approved, long userId);

    BookingResponseDto getById(long bookingId, long userId);

    Collection<BookingResponseDto> getAllByBookerId(BookingStatusFilter state, long userId, int from, int size);

    Collection<BookingResponseDto> getAllByItemOwnerId(BookingStatusFilter state, long userId, int from, int size);
}
