package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingMapperImpl implements BookingMapper {
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    @Override
    public Booking toBooking(BookingDtoRequest bookingDtoRequest, Item item, User user) {
        return new Booking()
                .setItem(item)
                .setBooker(user)
                .setStart(bookingDtoRequest.getStart())
                .setEnd(bookingDtoRequest.getEnd())
                .setStatus(BookingStatus.WAITING);
    }

    @Override
    public BookingDtoResponse toBookingDtoResponse(Booking booking) {
        return new BookingDtoResponse()
                .setId(booking.getId())
                .setStatus(booking.getStatus())
                .setStart(booking.getStart())
                .setEnd(booking.getEnd())
                .setItem(itemMapper.toItemDto(booking.getItem()))
                .setBooker(userMapper.toUserDto(booking.getBooker()));
    }

    @Override
    public BookingByItemDtoResponse toBookingInItemDtoResponse(Booking booking) {
        return new BookingByItemDtoResponse()
                .setId(booking.getId())
                .setStatus(booking.getStatus())
                .setStart(booking.getStart())
                .setEnd(booking.getEnd())
                .setItem(itemMapper.toItemDto(booking.getItem()))
                .setBookerId(booking.getBooker().getId());
    }
}
