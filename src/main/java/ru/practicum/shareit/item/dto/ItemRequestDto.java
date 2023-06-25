package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemRequestDto {
    protected long id;
    @NotBlank(message = "name can not be blank")
    protected String name;
    protected String description;
    protected Boolean available;
    protected long ownerId;
    protected ItemRequest request;
}
