package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Accessors(chain = true)
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}