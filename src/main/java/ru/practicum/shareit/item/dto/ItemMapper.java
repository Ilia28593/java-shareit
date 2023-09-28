package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingByItemDtoResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

@Component
public interface ItemMapper {
    ItemDto toItemDto(Item item);

    Collection<ItemDto> toItemCollection(Collection<Item> items);

    ItemDtoByBookingDto toItemDtoWithBookingDto(Item item,
                                                BookingByItemDtoResponse lastBooking,
                                                BookingByItemDtoResponse nextBooking,
                                                Collection<CommentResponseDto> comments);

    Item toItem(ItemDto itemDto, User user, ItemRequest itemRequest);
}
