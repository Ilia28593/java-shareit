package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingInItemDtoResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemMapperImpl implements ItemMapper {

    @Override
    public ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        if (item.getItemRequest() != null) {
            itemDto.setRequestId(item.getItemRequest().getId());
        }
        return itemDto;
    }

    @Override
    public Collection<ItemDto> toItemDtos(Collection<Item> items) {
        return items.stream().map(this::toItemDto).collect(Collectors.toList());
    }

    @Override
    public ItemDtoWithBookingDto toItemDtoWithBookingDto(Item item, BookingInItemDtoResponse lastBooking,
                                                         BookingInItemDtoResponse nextBooking,
                                                         Collection<CommentResponseDto> comments) {
        ItemDtoWithBookingDto itemDtoWithBookingDto = new ItemDtoWithBookingDto();
        itemDtoWithBookingDto.setId(item.getId());
        itemDtoWithBookingDto.setName(item.getName());
        itemDtoWithBookingDto.setDescription(item.getDescription());
        itemDtoWithBookingDto.setAvailable(item.getAvailable());
        itemDtoWithBookingDto.setLastBooking(lastBooking);
        itemDtoWithBookingDto.setNextBooking(nextBooking);
        itemDtoWithBookingDto.setComments(comments);
        if (item.getItemRequest() != null) {
            itemDtoWithBookingDto.setRequestId(item.getItemRequest().getId());
        }
        return itemDtoWithBookingDto;
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
