package ru.practicum.shareit.request;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.Collection;

@Service
public interface ItemRequestService {
    ItemRequestResponseDto create(ItemRequestDto itemRequestDto, long userId);

    Collection<ItemRequestResponseDto> findByUserId(long userId);

    Collection<ItemRequestResponseDto> findAll(int from, int size, long userId);

    ItemRequestResponseDto findById(long requestId, long userId);
}
