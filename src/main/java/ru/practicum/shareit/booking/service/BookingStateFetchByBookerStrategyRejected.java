package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.reposotory.BookingRepository;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.BookingStatusFilter;
import ru.practicum.shareit.user.User;

import java.util.Collection;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingStateFetchByBookerStrategyRejected implements BookingStateFetchByBookerStrategy {
    private final BookingRepository bookingRepository;

    @Override
    public BookingStatusFilter getStrategyName() {
        return BookingStatusFilter.REJECTED;
    }

    @Override
    public Collection<Booking> fetch(User user) {
        return bookingRepository.findBookingsByBookerAndStatusOrderByStartDesc(user, BookingStatus.REJECTED);
    }
}
