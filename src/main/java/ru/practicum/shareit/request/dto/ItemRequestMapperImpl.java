package ru.practicum.shareit.request.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestMapperImpl implements ItemRequestMapper {
    private final ItemMapper itemMapper;

    @Override
    public ItemRequestResponseDto toItemRequestDto(ItemRequest itemRequest) {
        ItemRequestResponseDto itemRequestResponseDto = new ItemRequestResponseDto()
                .setId(itemRequest.getId())
                .setDescription(itemRequest.getDescription())
                .setCreated(itemRequest.getCreated());
        if (itemRequest.getItems() != null) {
            itemRequestResponseDto.setItems(itemMapper.toItemCollection(itemRequest.getItems()));
        }
        return itemRequestResponseDto;
    }

    @Override
    public Collection<ItemRequestResponseDto> toItemRequestDtos(Collection<ItemRequest> itemRequests) {
        return itemRequests.stream().map(this::toItemRequestDto).collect(Collectors.toList());
    }

    @Override
    public ItemRequest toItemRequestItem(ItemRequestDto itemRequestDto, User user) {
        return new ItemRequest()
                .setDescription(itemRequestDto.getDescription())
                .setCreated(LocalDateTime.now())
                .setRequester(user);
    }
}
