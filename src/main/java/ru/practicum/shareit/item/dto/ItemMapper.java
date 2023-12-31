package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingInItemDtoResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.Collection;

@Component
public interface ItemMapper {
    ItemDto toItemDto(Item item);

    Collection<ItemDto> toItemDtos(Collection<Item> items);

    ItemDtoInBookingDto toItemDtoWithBookingDto(Item item,
                                                BookingInItemDtoResponse lastBooking,
                                                BookingInItemDtoResponse nextBooking,
                                                Collection<CommentResponseDto> comments);

    Item toItem(ItemDto itemDto, User user, ItemRequest itemRequest);
}
