package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.coment.dto.CommentConvertorCommentResponseDto;
import ru.practicum.shareit.coment.dto.CommentMapperImpl;
import ru.practicum.shareit.coment.dto.CommentRequestDto;
import ru.practicum.shareit.coment.dto.CommentResponseDto;
import ru.practicum.shareit.coment.model.Comment;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemController {

    private final ItemServiceImpl itemService;

    private final ItemMapperImpl mapper;

    private final CommentMapperImpl commentMapper;
    private final ItemConvertorItemResponseDto convertor;

    private final CommentConvertorCommentResponseDto convertorComment;

    @PostMapping
    public ItemResponseDto createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @RequestBody ItemRequestCreateDto requestDto) {
        Item item = mapper.mapToItem(requestDto);
        Item i = itemService.create(userId, item);
        return convertor.convert(i);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto createItem(@PathVariable long itemId,
                                         @RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestBody CommentRequestDto requestDto) {
        Comment comment = commentMapper.mapToComment(requestDto);
        Comment i = itemService.createComment(userId,itemId,comment);
        return convertorComment.convert(i);
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto updateItem(@PathVariable long itemId,
                                      @RequestHeader("X-Sharer-User-Id") long userId, @RequestBody ItemRequestDto requestDto) {
        return convertor.convert(itemService.update(userId, itemId, mapper.mapToItem(requestDto)));
    }

    @GetMapping("/{itemId}")
    public GetItemResponseDTO getById(@PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getByIdFull(userId, itemId);
    }

    @GetMapping
    public List<GetItemResponseDTO> getAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getAllByOwner(userId);
    }

    @GetMapping("/search")
    public List<ItemResponseDto> getFromDescription(@RequestHeader("X-Sharer-User-Id") long userId,
                                                    @RequestParam(name = "text") String description) {
        String d = description;
        itemService.getFromDescription(userId, description);
        return convertor.getListResponse(itemService.getFromDescription(userId, description));
    }
}
