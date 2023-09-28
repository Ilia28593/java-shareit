package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoByBookingDto;

import java.util.Collection;

@Service
public interface ItemService {
    ItemDtoByBookingDto getById(long itemId, long userId);

    ItemDto create(ItemDto itemDto, long userId);

    ItemDto update(ItemDto itemDto, long userId);

    void delete(long itemId);

    Collection<ItemDto> search(String text, long userId, int from, int size);

    CommentResponseDto addComment(CommentDto commentDto, long itemId, long userId);

    Collection<ItemDtoByBookingDto> findByUserId(long userId, int from, int size);
}
