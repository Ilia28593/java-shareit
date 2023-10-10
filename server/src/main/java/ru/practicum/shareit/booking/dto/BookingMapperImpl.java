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
    public Booking toBooking(BookingRequestDto bookingRequestDto, Item item, User user) {
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(bookingRequestDto.getStart());
        booking.setEnd(bookingRequestDto.getEnd());
        booking.setStatus(BookingStatus.WAITING);
        return booking;
    }

    @Override
    public BookingResponseDto toBookingDtoResponse(Booking booking) {
        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setId(booking.getId());
        bookingResponseDto.setStatus(booking.getStatus());
        bookingResponseDto.setStart(booking.getStart());
        bookingResponseDto.setEnd(booking.getEnd());
        bookingResponseDto.setItem(itemMapper.toItemDto(booking.getItem()));
        bookingResponseDto.setBooker(userMapper.toUserDto(booking.getBooker()));
        return bookingResponseDto;
    }

    @Override
    public BookingInItemResponseDto toBookingInItemDtoResponse(Booking booking) {
        BookingInItemResponseDto bookingDtoResponse = new BookingInItemResponseDto();
        bookingDtoResponse.setId(booking.getId());
        bookingDtoResponse.setStatus(booking.getStatus());
        bookingDtoResponse.setStart(booking.getStart());
        bookingDtoResponse.setEnd(booking.getEnd());
        bookingDtoResponse.setItem(itemMapper.toItemDto(booking.getItem()));
        bookingDtoResponse.setBookerId(booking.getBooker().getId());
        return bookingDtoResponse;
    }
}
