package ru.practicum.shareit.booking.BookingStateFetchByBooker;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatusFilter;
import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface BookingStateFetchByBookerStrategy {
    BookingStatusFilter getStrategyName();

    Collection<Booking> fetch(User user, Pageable pageable);
}
