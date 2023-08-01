package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Data
@Builder
public class ItemRequestCreateDto {
    protected long id;
    @NonNull
    protected String name;
    @NonNull
    protected String description;
    @NonNull
    protected Boolean available;
    protected User owner;
    protected ItemRequest request;
}
