package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.Accessors;
import ru.practicum.shareit.booking.dto.BookingByItemDtoResponse;
import ru.practicum.shareit.utils.Create;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Accessors(chain = true)
public class ItemDtoByBookingDto {
    private Long id;
    @NotEmpty(message = "name can not be empty", groups = {Create.class})
    @NotNull
    private String name;
    @NotEmpty(message = "description can not be empty", groups = {Create.class})
    @NotNull
    private String description;
    @NotNull(message = "available can not be empty", groups = {Create.class})
    private Boolean available;
    private BookingByItemDtoResponse lastBooking;
    private BookingByItemDtoResponse nextBooking;
    private Collection<CommentResponseDto> comments;
    private Long requestId;
}
