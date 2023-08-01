package ru.practicum.shareit.request.dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.model.ItemRequest;

@Component
public class RequestConvertorRequestResponseDto implements Converter<ItemRequest, ItemResponseResponseDto> {

    @Override
    public ItemResponseResponseDto convert(ItemRequest source) {
        return ItemResponseResponseDto.builder()
                .id(source.getId())
                .requestor(source.getRequestor())
                .description(source.getDescription())
                .created(source.getCreated())
                .build();
    }
}