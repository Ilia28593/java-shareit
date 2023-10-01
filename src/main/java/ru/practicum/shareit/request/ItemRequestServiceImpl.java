package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;
    private final ItemRequestMapper itemRequestMapper;

    @Override
    public ItemRequestResponseDto create(ItemRequestDto itemRequestDto, long userId) {
        User user = userService.getUserById(userId);
        ItemRequest itemRequest = itemRequestRepository.save(itemRequestMapper.toItemRequestItem(itemRequestDto, user));
        return itemRequestMapper.toItemRequestDto(itemRequest);

    }

    @Override
    public Collection<ItemRequestResponseDto> findByUserId(long userId) {
        User user = userService.getUserById(userId);
        Collection<ItemRequest> itemRequests = itemRequestRepository.findAllByRequester(user);
        return itemRequestMapper.toItemRequestDtos(itemRequests);
    }

    @Override
    public Collection<ItemRequestResponseDto> findAll(int from, int size, long userId) {
        User user = userService.getUserById(userId);
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return itemRequestMapper.toItemRequestDtos(
                itemRequestRepository.findAllByRequesterIsNot(user, pageRequest)
                        .stream()
                        .sorted(Comparator.comparing(ItemRequest::getCreated).reversed())
                        .collect(Collectors.toList()));
    }

    @Override
    public ItemRequestResponseDto findById(long requestId, long userId) {
        userService.getUserById(userId);
        ItemRequest itemRequest = itemRequestRepository
                .findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("itemRequest %s not found", requestId)));
        return itemRequestMapper.toItemRequestDto(itemRequest);
    }
}
