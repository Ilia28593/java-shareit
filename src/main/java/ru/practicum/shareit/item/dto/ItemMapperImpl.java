package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingByItemDtoResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemMapperImpl implements ItemMapper {

    @Override
    public ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto()
                .setId(item.getId())
                .setName(item.getName())
                .setDescription(item.getDescription())
                .setAvailable(item.getAvailable());
        if (item.getItemRequest() != null) {
            itemDto.setRequestId(item.getItemRequest().getId());
        }
        return itemDto;
    }

    @Override
    public Collection<ItemDto> toItemCollection(Collection<Item> items) {
        return items.stream().map(this::toItemDto).collect(Collectors.toList());
    }

    @Override
    public ItemDtoByBookingDto toItemDtoWithBookingDto(Item item, BookingByItemDtoResponse lastBooking,
                                                       BookingByItemDtoResponse nextBooking,
                                                       Collection<CommentResponseDto> comments) {
        ItemDtoByBookingDto itemDtoByBookingDto = new ItemDtoByBookingDto()
                .setId(item.getId())
                .setName(item.getName())
                .setDescription(item.getDescription())
                .setAvailable(item.getAvailable())
                .setLastBooking(lastBooking)
                .setNextBooking(nextBooking)
                .setComments(comments);
        if (item.getItemRequest() != null) {
            itemDtoByBookingDto.setRequestId(item.getItemRequest().getId());
        }
        return itemDtoByBookingDto;
    }

    @Override
    public Item toItem(ItemDto itemDto, User user, ItemRequest itemRequest) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(user);
        item.setItemRequest(itemRequest);
        return item;
    }
}
