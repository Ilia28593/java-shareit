package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.utils.Constants;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid BookingRequestDto requestDto,
                                         @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return bookingClient.create(requestDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(@PathVariable long bookingId,
                                          @RequestParam boolean approved,
                                          @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return bookingClient.approve(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(@PathVariable long bookingId,
                                          @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return bookingClient.getById(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByBookerId(@RequestHeader(Constants.HEADER_USER_ID) long userId,
                                                   @RequestParam(defaultValue = Constants.STATE_ALL) BookingState state,
                                                   @PositiveOrZero @RequestParam(defaultValue = Constants.PAGE_FROM_DEFAULT) Integer from,
                                                   @Positive @RequestParam(defaultValue = Constants.PAGE_SIZE_DEFAULT) Integer size) {
        return bookingClient.getAllByBookerId(state, userId, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllByItemOwnerId(@RequestParam(defaultValue = Constants.STATE_ALL) BookingState state,
                                                      @RequestHeader(Constants.HEADER_USER_ID) long userId,
                                                      @PositiveOrZero @RequestParam(defaultValue = Constants.PAGE_FROM_DEFAULT) int from,
                                                      @Positive @RequestParam(defaultValue = Constants.PAGE_SIZE_DEFAULT) int size) {
        return bookingClient.getAllByItemOwnerId(state, userId, from, size);
    }

}
