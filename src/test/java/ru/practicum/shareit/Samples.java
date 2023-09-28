package ru.practicum.shareit;

import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoInBookingDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

public class Samples {
    public static UserDto getUser1() {
        return new UserDto()
                .setId(1L)
                .setName("user_1")
                .setEmail("user1@email.ru");
    }

    public static UserDto getUser2() {
        return new UserDto()
                .setId(2L)
                .setName("user_2")
                .setEmail("user2@email.ru");
    }

    public static ItemDto getItem1() {
        return new ItemDto()
                .setId(1L)
                .setName("Name item 1")
                .setDescription("description item 1")
                .setAvailable(true);
    }

    public static ItemDto getItem2() {
        return new ItemDto()
                .setId(2L)
                .setName("Name item 2")
                .setDescription("description item 2")
                .setAvailable(true);
    }

    public static ItemDtoInBookingDto getItemResponse1(long id) {
        ItemDto itemDto = getItem1();
        return new ItemDtoInBookingDto()
                .setId(id)
                .setName(itemDto.getName())
                .setDescription(itemDto.getDescription())
                .setComments(Collections.emptyList())
                .setAvailable(itemDto.getAvailable());
    }

    public static ItemDtoInBookingDto getItemResponse2(long id) {
        ItemDto itemDto = getItem2();
        return new ItemDtoInBookingDto()
                .setId(id)
                .setName(itemDto.getName())
                .setDescription(itemDto.getDescription())
                .setComments(Collections.emptyList())
                .setAvailable(itemDto.getAvailable());
    }

    public static CommentResponseDto getCommentResponse(long id, String userName, LocalDateTime time) {
        return new CommentResponseDto()
                .setId(id)
                .setAuthorName(userName)
                .setCreated(time)
                .setText(getComment_1().getText());
    }

    public static CommentDto getComment_1() {
        return new CommentDto()
                .setText("comment 1");
    }

    public static BookingDtoRequest getBooking(long itemId) {
        return new BookingDtoRequest()
                .setItemId(itemId)
                .setStart(LocalDateTime.now().plusDays(1))
                .setEnd(LocalDateTime.now().plusDays(2));
    }

    public static BookingDtoResponse getBookingResponse(long id, long itemId) {
        return new BookingDtoResponse()
                .setId(id)
                .setItem(getItem1())
                .setStatus(BookingStatus.WAITING)
                .setBooker(getUser1())
                .setStart(getBooking(itemId).getStart())
                .setStart(getBooking(itemId).getEnd());
    }

    public static ItemRequestResponseDto getItemRequestResponseDto(long id, Collection<ItemDto> itemDtos, LocalDateTime created) {
        return new ItemRequestResponseDto()
                .setId(id)
                .setItems(itemDtos)
                .setCreated(created)
                .setDescription(getItemRequestDto().getDescription());
    }

    public static ItemRequestDto getItemRequestDto() {
        return new ItemRequestDto()
                .setDescription("item 1 request");
    }
}
