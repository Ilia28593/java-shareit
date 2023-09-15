package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatusFilter;
import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface BookingStateFetchByBookerStrategy {
    BookingStatusFilter getStrategyName();

    Collection<Booking> fetch(User user);
}
