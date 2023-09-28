package ru.practicum.shareit;

import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoByBookingDto;
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

    public static ItemDtoByBookingDto getItemResponse1(long id) {
        ItemDto itemDto = Fixtures.getItem1();
        ItemDtoByBookingDto itemDtoByBookingDto = new ItemDtoByBookingDto();
        itemDtoByBookingDto.setId(id);
        itemDtoByBookingDto.setName(itemDto.getName());
        itemDtoByBookingDto.setDescription(itemDto.getDescription());
        itemDtoByBookingDto.setComments(Collections.emptyList());
        itemDtoByBookingDto.setAvailable(itemDto.getAvailable());
        return itemDtoByBookingDto;
    }

    public static ItemDto getItem2() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(2L);
        itemDto.setName("Item name 2");
        itemDto.setDescription("Item description 2");
        itemDto.setAvailable(true);
        return itemDto;
    }

    public static ItemDtoByBookingDto getItemResponse2(long id) {
        ItemDto itemDto = Fixtures.getItem2();
        ItemDtoByBookingDto itemDtoByBookingDto = new ItemDtoByBookingDto();
        itemDtoByBookingDto.setId(id);
        itemDtoByBookingDto.setName(itemDto.getName());
        itemDtoByBookingDto.setDescription(itemDto.getDescription());
        itemDtoByBookingDto.setComments(Collections.emptyList());
        itemDtoByBookingDto.setAvailable(itemDto.getAvailable());
        return itemDtoByBookingDto;
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

    public static BookingDtoRequest getBooking(long itemId) {
        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest();
        bookingDtoRequest.setItemId(itemId);
        bookingDtoRequest.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoRequest.setEnd(LocalDateTime.now().plusDays(2));
        return bookingDtoRequest;
    }

    public static BookingDtoResponse getBookingResponse(long id, long itemId) {
        BookingDtoResponse bookingDtoResponse = new BookingDtoResponse();
        bookingDtoResponse.setId(id);
        bookingDtoResponse.setItem(getItem1());
        bookingDtoResponse.setStatus(BookingStatus.WAITING);
        bookingDtoResponse.setBooker(getUser1());
        bookingDtoResponse.setStart(getBooking(itemId).getStart());
        bookingDtoResponse.setEnd(getBooking(itemId).getEnd());
        return bookingDtoResponse;
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
