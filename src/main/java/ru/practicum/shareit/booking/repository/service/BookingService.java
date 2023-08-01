package ru.practicum.shareit.booking.repository.service;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {

    Booking create(long idUser, long idItem, Booking booking);

    Booking approvedOrRejectedBooking(long userId, long bookingId, boolean approved);

    Booking getInfoBooking(long userId, long bookingId);

    List<Booking> getListUserBooking(long userId, String status);

    List<Booking>  getUserListBooking(long userId, String status);


}
