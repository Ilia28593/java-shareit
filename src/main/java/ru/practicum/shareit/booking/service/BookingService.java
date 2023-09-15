package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.BookingStatusFilter;

import java.util.Collection;

public interface BookingService {
    BookingDtoResponse create(BookingDtoRequest bookingDtoRequest, long userId);

    BookingDtoResponse approve(long bookingId, boolean approved, long userId);

    BookingDtoResponse getById(long bookingId, long userId);

    Collection<BookingDtoResponse> allFromBookerId(BookingStatusFilter state, long userId);

    Collection<BookingDtoResponse> allFromItemOwnerId(BookingStatusFilter state, long userId);
}
