package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.utils.Constants;
import ru.practicum.shareit.utils.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@PathVariable long itemId, @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemClient.getById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findByUserId(@RequestHeader(Constants.HEADER_USER_ID) long userId,
                                               @PositiveOrZero @RequestParam(defaultValue = Constants.PAGE_FROM_DEFAULT) int from,
                                               @Positive @RequestParam(defaultValue = Constants.PAGE_SIZE_DEFAULT) int size) {
        return itemClient.findByUserId(userId, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> create(@Validated(Create.class) @RequestBody ItemDto itemDto,
                                         @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemClient.create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestBody ItemDto itemDto, @PathVariable long itemId,
                                         @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemClient.update(itemDto, itemId, userId);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@PathVariable long itemId, @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        itemClient.delete(itemId, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text,
                                         @RequestHeader(Constants.HEADER_USER_ID) long userId,
                                         @PositiveOrZero @RequestParam(defaultValue = Constants.PAGE_FROM_DEFAULT) int from,
                                         @Positive @RequestParam(defaultValue = Constants.PAGE_SIZE_DEFAULT) int size) {
        return itemClient.search(text, userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@Validated @RequestBody CommentDto commentDto,
                                             @PathVariable long itemId,
                                             @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemClient.addComment(commentDto, itemId, userId);
    }
}
