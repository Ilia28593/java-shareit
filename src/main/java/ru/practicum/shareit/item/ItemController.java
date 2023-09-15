package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.config.Create;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

import static ru.practicum.shareit.config.Constants.HEADER_USER_ID;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemDtoWithBookingDto getById(@PathVariable long itemId, @RequestHeader(HEADER_USER_ID) long userId) {
        return itemService.getById(itemId, userId);
    }


    @GetMapping
    public Collection<ItemDtoWithBookingDto> findByUserId(@RequestHeader(HEADER_USER_ID) long userId) {
        return itemService.findByUserId(userId);
    }

    @PostMapping
    public ItemDto create(@Validated(Create.class) @RequestBody ItemDto itemDto,
                          @RequestHeader(HEADER_USER_ID) long userId) {
        return itemService.create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto, @PathVariable long itemId,
                          @RequestHeader(HEADER_USER_ID) long userId) {
        itemDto.setId(itemId);
        return itemService.update(itemDto, userId);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@PathVariable long itemId) {
        itemService.delete(itemId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> search(@RequestParam String text,
                                      @RequestHeader(HEADER_USER_ID) long userId) {
        return itemService.search(text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addComment(@Validated @RequestBody CommentDto commentDto,
                                         @PathVariable long itemId,
                                         @RequestHeader(HEADER_USER_ID) long userId) {
        return itemService.addComment(commentDto, itemId, userId);
    }
}
