package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class ItemRequestDto {
    protected long id;
    @NotBlank(message = "name can not be blank")
    protected String name;
    protected String description;
    protected Boolean available;
    protected User owner;
    protected ItemRequest request;
}
