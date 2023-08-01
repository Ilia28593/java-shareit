package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemRequestCreateDto {
    protected long id;
    @NotBlank(message = "name can not be blank")
    @NonNull
    protected String name;
    @NonNull
    protected String description;
    @NonNull
    protected Boolean available;
    protected long ownerId;
    protected ItemRequest request;
}
