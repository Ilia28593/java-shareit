package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.BookingStatusFilter;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.constraints.Min;
import java.util.Collection;

import static ru.practicum.shareit.config.Constants.*;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDtoResponse create(@Validated @RequestBody BookingDtoRequest bookingDtoRequest,
                                     @RequestHeader(HEADER_USER_ID) long userId) {
        return bookingService.create(bookingDtoRequest, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoResponse approve(@PathVariable long bookingId,
                                      @RequestParam boolean approved,
                                      @RequestHeader(HEADER_USER_ID) long userId) {
        return bookingService.approved(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoResponse getById(@PathVariable long bookingId,
                                      @RequestHeader(HEADER_USER_ID) long userId) {
        return bookingService.getById(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingDtoResponse> getAllByBookerId(@RequestParam(defaultValue = "ALL") BookingStatusFilter state,
                                                           @RequestHeader(HEADER_USER_ID) long userId,
                                                           @RequestParam(defaultValue = PAGE_DEF) @Min(0) int from,
                                                           @RequestParam(defaultValue = PAGE_DEF_SIZE) @Min(1) int size) {
        return bookingService.getAllByBookerId(state, userId, from, size);
    }

    @GetMapping("/owner")
    public Collection<BookingDtoResponse> getAllByItemOwnerId(@RequestParam(defaultValue = "ALL") BookingStatusFilter state,
                                                              @RequestHeader(HEADER_USER_ID) long userId,
                                                              @RequestParam(defaultValue = PAGE_DEF) @Min(0) int from,
                                                              @RequestParam(defaultValue = PAGE_DEF_SIZE) @Min(1) int size) {
        return bookingService.getAllByItemOwnerId(state, userId, from, size);
    }
}
