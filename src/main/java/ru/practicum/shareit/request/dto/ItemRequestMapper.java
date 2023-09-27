package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface ItemRequestMapper {

    ItemRequestResponseDto toItemRequestDto(ItemRequest itemRequest);

    Collection<ItemRequestResponseDto> toItemRequestDtos(Collection<ItemRequest> itemRequests);

    ItemRequest toItemRequestItem(ItemRequestDto itemRequestDto, User userId);
}
