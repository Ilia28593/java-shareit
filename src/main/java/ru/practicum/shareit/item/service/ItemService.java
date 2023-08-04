package ru.practicum.shareit.item.service;

import ru.practicum.shareit.coment.model.Comment;
import ru.practicum.shareit.item.dto.GetItemResponseDTO;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item create(Long idUser, Item entity);

    Item update(long userId, long itemId, Item entity);

    List<Item> getFromDescription(long userId, String text);

    List<GetItemResponseDTO> getAllByOwner(long userId);

    GetItemResponseDTO getByIdFull(long userId, long itemId);

    Comment createComment(long userId, long itemId, Comment comment);
}
