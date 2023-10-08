package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.utils.Constants;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(@Validated @RequestBody ItemRequestDto itemRequestDto,
                                         @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemRequestClient.create(itemRequestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllByUserId(@RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemRequestClient.findByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAll(@PositiveOrZero @RequestParam(required = false) Integer from,
                                          @Positive @RequestParam(required = false) Integer size,
                                          @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemRequestClient.findAll(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findRequestById(@PathVariable long requestId,
                                                  @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return itemRequestClient.findById(requestId, userId);
    }
}
