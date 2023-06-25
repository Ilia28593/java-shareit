package ru.practicum.shareit.item.dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemConvertorItemResponseDto implements Converter<Item, ItemResponseDto> {
    @Override
    public ItemResponseDto convert(Item source) {
        return new ItemResponseDto(
                source.getId(),
                source.getName(),
                source.getDescription(),
                source.getAvailable(),
                source.getRequest());
    }

    public List<ItemResponseDto> getListResponse(List<Item> list) {
        return list.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
