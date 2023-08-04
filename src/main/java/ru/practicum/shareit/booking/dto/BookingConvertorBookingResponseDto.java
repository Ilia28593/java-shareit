package ru.practicum.shareit.booking.dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemResponseNameDto;
import ru.practicum.shareit.user.dto.UserIdDto;

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
                .booker(UserIdDto.builder()
                        .id(source.getBooker().getId())
                        .build())
                .status(source.getBookingStatus())
                .item(ItemResponseNameDto.builder()
                        .name(source.getItem().getName())
                        .id(source.getItem().getId())
                        .build())
                .build();
    }

    public List<BookingResponseDto> getListResponse(List<Booking> list) {
        return list.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
