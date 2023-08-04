package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingResponseFromGetItems;
import ru.practicum.shareit.coment.dto.CommentResponseDto;

import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
public class GetItemResponseDTO {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingResponseFromGetItems lastBooking;
    private BookingResponseFromGetItems nextBooking;
    private List<CommentResponseDto> comments;
}
