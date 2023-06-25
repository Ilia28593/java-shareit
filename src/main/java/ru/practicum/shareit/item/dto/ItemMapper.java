package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.model.Item;

@Mapper
public interface ItemMapper {
    Item mapToItem(ItemRequestDto dto);
}