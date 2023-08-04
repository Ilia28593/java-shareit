package ru.practicum.shareit.booking.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;

import javax.annotation.processing.Generated;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2023-06-14T20:27:59+0300",
        comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Amazon.com Inc.)"
)
@Component
public class BookingMapperImpl implements BookingMapper {

    @Override
    public Booking mapToBooking(BookingRequestDto dto) {
        if (dto == null) {
            return null;
        }
        return Booking.builder()
                .start(dto.getStart())
                .end(dto.getEnd())
                .item(dto.getItem())
                .booker(dto.getBooker())
                .bookingStatus(dto.getBookingStatus())
                .build();
    }

    public Booking mapToBooking(BookingRequest dto) {
        if (dto == null) {
            return null;
        }
        return Booking.builder()
                .start(dto.getStart())
                .end(dto.getEnd())
                .build();
    }
}

