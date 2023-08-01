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
        return BookingResponseDto.builder()
                .id(source.getId())
                .start(source.getStart())
                .end(source.getEnd())
                .item(source.getItem())
                .booker(source.getBooker())
                .bookingStatus(source.getBookingStatus())
                .build();
    }

    public List<BookingResponseDto> getListResponse(List<Booking> list) {
        return list.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
