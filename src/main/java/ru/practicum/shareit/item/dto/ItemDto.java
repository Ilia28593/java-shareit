package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.Accessors;
import ru.practicum.shareit.config.Create;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Accessors(chain = true)
public class ItemDto {
    private Long id;
    @NotEmpty(message = "name can not be empty", groups = {Create.class})
    private String name;
    @NotEmpty(message = "description can not be empty", groups = {Create.class})
    private String description;
    @NotNull(message = "available can not be empty", groups = {Create.class})
    private Boolean available;
    private Long requestId;
}