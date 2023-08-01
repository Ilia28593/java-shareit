package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemController {

    private final ItemService itemService;

    private final ItemMapperImpl mapper;

    private final ItemConvertorItemResponseDto convertor;

    @PostMapping
    public ItemResponseDto createItem(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody ItemRequestCreateDto requestDto) {
        return convertor.convert(itemService.create(userId, mapper.mapToItem(requestDto)));
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto updateItem(@PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId, @RequestBody ItemRequestDto requestDto) {
        return convertor.convert(itemService.update(userId, itemId, mapper.mapToItem(requestDto)));
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto getById(@PathVariable long itemId) {
        return convertor.convert(itemService.getById(itemId));
    }

    @GetMapping
    public List<ItemResponseDto> getAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        return convertor.getListResponse(itemService.getAll(userId));
    }

    @GetMapping("/search")
    public List<ItemResponseDto> getFromDescription(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(name = "text") String description) {
        String d = description;
        itemService.getFromDescription(userId, description);
        return convertor.getListResponse(itemService.getFromDescription(userId, description));
    }
}
