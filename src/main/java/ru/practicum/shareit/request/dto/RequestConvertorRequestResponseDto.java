package ru.practicum.shareit.request.dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.model.ItemRequest;

@Component
public class RequestConvertorRequestResponseDto implements Converter<ItemRequest, ItemResponseResponseDto> {

    @Override
    public ItemResponseResponseDto convert(ItemRequest source) {
        ItemResponseResponseDto itemRequest = new ItemResponseResponseDto();
        itemRequest.setId(source.getId());
        itemRequest.setRequestor(source.getRequestor());
        itemRequest.setDescription(source.getDescription());
        itemRequest.setCreated(source.getCreated());
        return itemRequest;
    }
}