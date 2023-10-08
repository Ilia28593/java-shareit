package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingInItemResponseDto;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ItemDtoWithBookingDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingInItemResponseDto lastBooking;
    private BookingInItemResponseDto nextBooking;
    private Collection<CommentResponseDto> comments;
    private Long requestId;
}
