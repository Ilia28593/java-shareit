package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import javax.annotation.processing.Generated;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2023-06-14T20:27:59+0300",
        comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Amazon.com Inc.)"
)
@Component
public class ItemMapperImpl implements ItemMapper {
    @Override
    public Item mapToItem(ItemRequestDto dto) {
        if (dto == null) {
            return null;
        }
        return new Item (dto.getId(), dto.getName(), dto.getDescription(), dto.getAvailable(), dto.getOwnerId(), dto.request);
    }

    public Item  mapToItem(ItemRequestCreateDto dto) {
        if (dto == null) {
            return null;
        }
        return new Item (dto.getId(), dto.getName(), dto.getDescription(), dto.getAvailable(), dto.getOwnerId(), dto.request);
    }
}

