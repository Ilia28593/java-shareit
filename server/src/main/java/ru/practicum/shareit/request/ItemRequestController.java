package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.utils.Constants;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestResponseDto create(@RequestBody ItemRequestDto itemRequestDto,
                                         @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemRequestService.create(itemRequestDto, userId);
    }

    @GetMapping
    public Collection<ItemRequestResponseDto> findAllByUserId(@RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemRequestService.findByUserId(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestResponseDto> findAll(@RequestParam(required = false) Integer from,
                                                      @RequestParam(required = false) Integer size,
                                                      @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        if (from != null && size != null) {
            return itemRequestService.findAll(from, size, userId);
        }
        return Collections.emptyList();
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto findRequestById(@PathVariable long requestId,
                                                  @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemRequestService.findById(requestId, userId);
    }
}
