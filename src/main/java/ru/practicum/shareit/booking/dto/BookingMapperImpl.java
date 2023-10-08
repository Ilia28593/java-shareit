package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserMapper;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingMapperImpl implements BookingMapper {
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    @Override
    public Booking toBooking(BookingDtoRequest bookingDtoRequest, Item item, User user) {
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(bookingDtoRequest.getStart());
        booking.setEnd(bookingDtoRequest.getEnd());
        booking.setStatus(BookingStatus.WAITING);
        return booking;
    }

    @Override
    public BookingDtoResponse toBookingDtoResponse(Booking booking) {
        BookingDtoResponse bookingDtoResponse = new BookingDtoResponse();
        bookingDtoResponse.setId(booking.getId());
        bookingDtoResponse.setStatus(booking.getStatus());
        bookingDtoResponse.setStart(booking.getStart());
        bookingDtoResponse.setEnd(booking.getEnd());
        bookingDtoResponse.setItem(itemMapper.toItemDto(booking.getItem()));
        bookingDtoResponse.setBooker(userMapper.toUserDto(booking.getBooker()));
        return bookingDtoResponse;
    }

    @Override
    public BookingInItemDtoResponse toBookingInItemDtoResponse(Booking booking) {
        BookingInItemDtoResponse bookingDtoResponse = new BookingInItemDtoResponse();
        bookingDtoResponse.setId(booking.getId());
        bookingDtoResponse.setStatus(booking.getStatus());
        bookingDtoResponse.setStart(booking.getStart());
        bookingDtoResponse.setEnd(booking.getEnd());
        bookingDtoResponse.setItem(itemMapper.toItemDto(booking.getItem()));
        bookingDtoResponse.setBookerId(booking.getBooker().getId());
        return bookingDtoResponse;
    }
}
