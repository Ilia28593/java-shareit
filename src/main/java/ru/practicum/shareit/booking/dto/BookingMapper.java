package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public interface BookingMapper {
    Booking toBooking(BookingDtoRequest bookingDtoRequest, Item item, User user);

    BookingDtoResponse toBookingDtoResponse(Booking booking);

    BookingInItemDtoResponse toBookingInItemDtoResponse(Booking booking);
}
