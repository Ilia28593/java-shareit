package ru.practicum.shareit.booking.dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookingConvertorBookingResponseDto implements Converter<Booking, BookingResponseDto> {

    @Override
    public BookingResponseDto convert(Booking source) {
        return new BookingResponseDto(
                source.getId(),
                source.getStart(),
                source.getEnd(),
                source.getItem(),
                source.getBooker(),
                source.getBookingStatus());
    }

    public List<BookingResponseDto> getListResponse(List<Booking> list) {
        return list.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
