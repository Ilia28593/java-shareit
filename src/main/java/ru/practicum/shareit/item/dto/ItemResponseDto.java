package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
public class ItemResponseDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
}
