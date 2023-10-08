package ru.practicum.shareit.booking.BookingStateFetchByBooker;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatusFilter;
import ru.practicum.shareit.config.DataUtilsService;
import ru.practicum.shareit.user.User;

import java.util.Collection;

import static ru.practicum.shareit.config.DataUtilsService.now;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingStateFetchByBookerStrategyCurrent implements BookingStateFetchByBookerStrategy {
    private final BookingRepository bookingRepository;

    @Override
    public BookingStatusFilter getStrategyName() {
        return BookingStatusFilter.CURRENT;
    }

    @Override
    public Collection<Booking> fetch(User user, Pageable pageable) {
        return bookingRepository.findBookingsByBookerAndStartBeforeAndEndAfter(user, now(), now(), pageable);
    }
}
