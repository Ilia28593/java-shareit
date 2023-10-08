package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.utils.Constants;

import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto create(@RequestBody BookingRequestDto bookingRequestDto,
                                     @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return bookingService.create(bookingRequestDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approve(@PathVariable long bookingId,
                                      @RequestParam boolean approved,
                                      @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return bookingService.approve(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getById(@PathVariable long bookingId,
                                      @RequestHeader(Constants.HEADER_USER_ID) long userId) {
        return bookingService.getById(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingResponseDto> getAllByBookerId(@RequestParam(defaultValue = "ALL") BookingStatusFilter state,
                                                           @RequestHeader(Constants.HEADER_USER_ID) long userId,
                                                           @RequestParam(defaultValue = Constants.PAGE_FROM_DEFAULT) int from,
                                                           @RequestParam(defaultValue = Constants.PAGE_SIZE_DEFAULT) int size) {
        return bookingService.getAllByBookerId(state, userId, from, size);
    }

    @GetMapping("/owner")
    public Collection<BookingResponseDto> getAllByItemOwnerId(@RequestParam(defaultValue = "ALL") BookingStatusFilter state,
                                                              @RequestHeader(Constants.HEADER_USER_ID) long userId,
                                                              @RequestParam(defaultValue = Constants.PAGE_FROM_DEFAULT) int from,
                                                              @RequestParam(defaultValue = Constants.PAGE_SIZE_DEFAULT) int size) {
        return bookingService.getAllByItemOwnerId(state, userId, from, size);
    }
}
