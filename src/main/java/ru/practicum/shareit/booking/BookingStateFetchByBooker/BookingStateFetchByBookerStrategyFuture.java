package ru.practicum.shareit.booking.BookingStateFetchByBooker;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.BookingStatusFilter;
import ru.practicum.shareit.config.DataUtilsService;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingStateFetchByBookerStrategyFuture implements BookingStateFetchByBookerStrategy {
    private final BookingRepository bookingRepository;
    private final DataUtilsService dataUtils;

    @Override
    public BookingStatusFilter getStrategyName() {
        return BookingStatusFilter.FUTURE;
    }

    @Override
    public Collection<Booking> fetch(User user, Pageable pageable) {
        return bookingRepository.findBookingsByBookerAndStatusInAndStartAfter(
                user, List.of(BookingStatus.APPROVED, BookingStatus.WAITING), dataUtils.getCurrentLocalTime(), pageable);
    }
}
