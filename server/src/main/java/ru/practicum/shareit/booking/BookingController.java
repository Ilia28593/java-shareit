package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.config.Constants;

import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDtoResponse create(@RequestBody BookingDtoRequest bookingDtoRequest,
                                     @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return bookingService.create(bookingDtoRequest, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoResponse approve(@PathVariable long bookingId,
                                      @RequestParam boolean approved,
                                      @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return bookingService.approve(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoResponse getById(@PathVariable long bookingId,
                                      @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return bookingService.getById(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingDtoResponse> getAllByBookerId(@RequestParam(defaultValue = "ALL") BookingStatusFilter state,
                                                           @RequestHeader(Constants.HEADER_USER_ID) long userId,
                                                           @RequestParam(defaultValue = Constants.PAGE_FROM_DEFAULT) int from,
                                                           @RequestParam(defaultValue = Constants.PAGE_SIZE_DEFAULT) int size) {
        return bookingService.getAllByBookerId(state, userId, from, size);
    }

    @GetMapping("/owner")
    public Collection<BookingDtoResponse> getAllByItemOwnerId(@RequestParam(defaultValue = "ALL") BookingStatusFilter state,
                                                              @RequestHeader(Constants.HEADER_USER_ID) long userId,
                                                              @RequestParam(defaultValue = Constants.PAGE_FROM_DEFAULT) int from,
                                                              @RequestParam(defaultValue = Constants.PAGE_SIZE_DEFAULT) int size) {
        return bookingService.getAllByItemOwnerId(state, userId, from, size);
    }
}
