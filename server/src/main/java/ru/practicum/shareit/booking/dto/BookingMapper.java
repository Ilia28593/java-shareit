package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

public interface BookingMapper {
    Booking toBooking(BookingRequestDto bookingRequestDto, Item item, User user);

    BookingResponseDto toBookingDtoResponse(Booking booking);

    BookingInItemResponseDto toBookingInItemDtoResponse(Booking booking);
}
