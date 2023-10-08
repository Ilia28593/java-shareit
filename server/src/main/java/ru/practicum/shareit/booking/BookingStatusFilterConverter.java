package ru.practicum.shareit.booking;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.UnsupportedBookingStatusFilterException;

@Component
public class BookingStatusFilterConverter implements Converter<String, BookingStatusFilter> {
    @Override
    public BookingStatusFilter convert(String source) {
        try {
            return BookingStatusFilter.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedBookingStatusFilterException("Unknown state: " + source);
        }
    }
}
