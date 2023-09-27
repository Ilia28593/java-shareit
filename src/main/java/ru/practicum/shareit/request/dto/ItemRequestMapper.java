package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface ItemRequestMapper {

    ItemRequestResponseDto toItemRequestDto(ItemRequest itemRequest);

    Collection<ItemRequestResponseDto> toItemRequestDtos(Collection<ItemRequest> itemRequests);

    ItemRequest toItemRequestItem(ItemRequestDto itemRequestDto, User userId);
}