package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Getter
@Setter
@Builder(toBuilder = true)
public class ItemResponseDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
}
