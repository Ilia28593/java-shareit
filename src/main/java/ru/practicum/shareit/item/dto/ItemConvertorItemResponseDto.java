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
        return ItemResponseDto.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .available(source.getAvailable())
                .request(source.getRequest())
                .build();
    }

    public List<ItemResponseDto> getListResponse(List<Item> list) {
        return list.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
