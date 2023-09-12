package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.reposotory.BookingRepository;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.BookingStatusFilter;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingStateFetchByBookerStrategyPast implements BookingStateFetchByBookerStrategy {
    private final BookingRepository bookingRepository;

    @Override
    public BookingStatusFilter getStrategyName() {
        return BookingStatusFilter.PAST;
    }

    @Override
    public Collection<Booking> fetch(User user) {
        return bookingRepository.findBookingsByBookerAndStatusInAndEndBeforeOrderByStartDesc(
                user, List.of(BookingStatus.APPROVED, BookingStatus.WAITING), LocalDateTime.now());
    }
}
