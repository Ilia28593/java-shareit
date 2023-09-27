package ru.practicum.shareit.booking.BookingStateFetchByBooker;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.BookingStatusFilter;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.user.User;

import java.util.Collection;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingStateFetchByBookerStrategyWaiting implements BookingStateFetchByBookerStrategy {
    private final BookingRepository bookingRepository;

    @Override
    public BookingStatusFilter getStrategyName() {
        return BookingStatusFilter.WAITING;
    }

    @Override
    public Collection<Booking> fetch(User user, Pageable pageable) {
        return bookingRepository.findBookingsByBookerAndStatus(user, BookingStatus.WAITING, pageable);
    }
}
