package ru.practicum.shareit;

import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

public class Fixtures {
    public static UserDto getUser1() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("user1");
        userDto.setEmail("test1@email.ru");
        return userDto;
    }

    public static UserDto getUser2() {
        UserDto userDto = new UserDto();
        userDto.setId(2L);
        userDto.setName("user2");
        userDto.setEmail("test2@yandex.ru");
        return userDto;
    }

    public static ItemDto getItem1() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Item name 1");
        itemDto.setDescription("Item description 1");
        itemDto.setAvailable(true);
        return itemDto;
    }

    public static ItemDtoWithBookingDto getItemResponse1(long id) {
        ItemDto itemDto = Fixtures.getItem1();
        ItemDtoWithBookingDto itemDtoWithBookingDto = new ItemDtoWithBookingDto();
        itemDtoWithBookingDto.setId(id);
        itemDtoWithBookingDto.setName(itemDto.getName());
        itemDtoWithBookingDto.setDescription(itemDto.getDescription());
        itemDtoWithBookingDto.setComments(Collections.emptyList());
        itemDtoWithBookingDto.setAvailable(itemDto.getAvailable());
        return itemDtoWithBookingDto;
    }

    public static ItemDto getItem2() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(2L);
        itemDto.setName("Item name 2");
        itemDto.setDescription("Item description 2");
        itemDto.setAvailable(true);
        return itemDto;
    }

    public static ItemDtoWithBookingDto getItemResponse2(long id) {
        ItemDto itemDto = Fixtures.getItem2();
        ItemDtoWithBookingDto itemDtoWithBookingDto = new ItemDtoWithBookingDto();
        itemDtoWithBookingDto.setId(id);
        itemDtoWithBookingDto.setName(itemDto.getName());
        itemDtoWithBookingDto.setDescription(itemDto.getDescription());
        itemDtoWithBookingDto.setComments(Collections.emptyList());
        itemDtoWithBookingDto.setAvailable(itemDto.getAvailable());
        return itemDtoWithBookingDto;
    }

    public static CommentDto getComment() {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("comment item 1");
        return commentDto;
    }

    public static CommentResponseDto getCommentResponse(long id, String userName, LocalDateTime time) {
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setId(id);
        commentResponseDto.setAuthorName(userName);
        commentResponseDto.setCreated(time);
        commentResponseDto.setText(getComment().getText());
        return commentResponseDto;
    }

    public static BookingRequestDto getBooking(long itemId) {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(itemId);
        bookingRequestDto.setStart(LocalDateTime.now().plusDays(1));
        bookingRequestDto.setEnd(LocalDateTime.now().plusDays(2));
        return bookingRequestDto;
    }

    public static BookingResponseDto getBookingResponse(long id, long itemId) {
        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setId(id);
        bookingResponseDto.setItem(getItem1());
        bookingResponseDto.setStatus(BookingStatus.WAITING);
        bookingResponseDto.setBooker(getUser1());
        bookingResponseDto.setStart(getBooking(itemId).getStart());
        bookingResponseDto.setEnd(getBooking(itemId).getEnd());
        return bookingResponseDto;
    }

    public static ItemRequestDto getItemRequestDto() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("item1 request");
        return itemRequestDto;
    }

    public static ItemRequestResponseDto getItemRequestResponseDto(long id, Collection<ItemDto> itemDtos, LocalDateTime created) {
        ItemRequestResponseDto itemRequestResponseDto = new ItemRequestResponseDto();
        itemRequestResponseDto.setId(id);
        itemRequestResponseDto.setItems(itemDtos);
        itemRequestResponseDto.setCreated(created);
        itemRequestResponseDto.setDescription(getItemRequestDto().getDescription());
        return itemRequestResponseDto;
    }
}
