package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.model.Booking;

@Mapper
public interface BookingMapper {
    Booking mapToBooking(BookingRequestDto dto);
}
