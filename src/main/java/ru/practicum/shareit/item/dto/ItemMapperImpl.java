package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingInItemDtoResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.Collection;

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
        return itemDto;
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
        return itemDtoWithBookingDto;
    }

    @Override
    public Item toItem(ItemDto itemDto, User user) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(user);
        return item;
    }
}
