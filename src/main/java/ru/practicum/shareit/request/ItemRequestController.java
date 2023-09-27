package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.utils.Constants;

import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestResponseDto create(@Validated @RequestBody ItemRequestDto itemRequestDto,
                                         @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemRequestService.create(itemRequestDto, userId);
    }

    @GetMapping
    public Collection<ItemRequestResponseDto> findAllByUserId(@RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemRequestService.findByUserId(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestResponseDto> findAll(@RequestParam(required = false) @Min(0) Integer from,
                                                      @RequestParam(required = false) @Min(1) Integer size,
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
