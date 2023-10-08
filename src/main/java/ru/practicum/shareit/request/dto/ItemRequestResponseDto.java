package ru.practicum.shareit.request.dto;

import lombok.*;
import lombok.experimental.Accessors;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Accessors(chain = true)
public class ItemRequestResponseDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private Collection<ItemDto> items;
}
