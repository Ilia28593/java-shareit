package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Collection;

import static ru.practicum.shareit.utils.Constants.REQUEST_NO_FOUND_BY_ID;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserServiceImpl userService;
    private final ItemRequestMapper itemRequestMapper;

    @Override
    public ItemRequestResponseDto create(ItemRequestDto itemRequestDto, long userId) {
        User user = userService.findById(userId);
        ItemRequest itemRequest = itemRequestRepository.save(itemRequestMapper.toItemRequestItem(itemRequestDto, user));
        return itemRequestMapper.toItemRequestDto(itemRequest);

    }

    @Override
    public Collection<ItemRequestResponseDto> findByUserId(long userId) {
        User user = userService.findById(userId);
        Collection<ItemRequest> itemRequests = itemRequestRepository.findAllByRequester(user);
        return itemRequestMapper.toItemRequestDtos(itemRequests);
    }

    @Override
    public Collection<ItemRequestResponseDto> findAll(int from, int size, long userId) {
        User user = userService.findById(userId);
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return itemRequestMapper.toItemRequestDtos(itemRequestRepository.findAllByRequesterIsNot(user, pageRequest));
    }

    @Override
    public ItemRequestResponseDto findById(long requestId, long userId) {
        userService.findById(userId);
        ItemRequest itemRequest = findById(requestId);
        return itemRequestMapper.toItemRequestDto(itemRequest);
    }

    public ItemRequest findById(long requestId) {
        return itemRequestRepository
                .findById(requestId)
                .orElseThrow(() -> new NotFoundException(REQUEST_NO_FOUND_BY_ID + requestId));
    }
}
