package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;

@Data
@Builder
public class ItemResponseDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private ItemRequest request;
}
