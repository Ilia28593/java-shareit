package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingInItemDtoResponse;
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
    public Collection<ItemDto> toItemDtos(Collection<Item> items) {
        return items.stream().map(this::toItemDto).collect(Collectors.toList());
    }

    @Override
    public ItemDtoInBookingDto toItemDtoWithBookingDto(Item item, BookingInItemDtoResponse lastBooking,
                                                       BookingInItemDtoResponse nextBooking,
                                                       Collection<CommentResponseDto> comments) {
        ItemDtoInBookingDto itemDtoByBookingDto = new ItemDtoInBookingDto()
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
        return new Item()
                .setId(itemDto.getId())
                .setName(itemDto.getName())
                .setDescription(itemDto.getDescription())
                .setAvailable(itemDto.getAvailable())
                .setOwner(user)
                .setItemRequest(itemRequest);
    }
}
