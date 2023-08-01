package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingConvertorBookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingMapperImpl;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.repository.service.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingController {

    private final BookingMapperImpl mapper;

    private final BookingConvertorBookingResponseDto convertor;

    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto createBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestBody BookingRequest requestDto) {
        return convertor.convert(bookingService.create(userId, requestDto.getItemId(), mapper.mapToBooking(requestDto)));
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approvedOrRejectedBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                                        @PathVariable long bookingId,
                                                        @RequestParam(name = "approved") boolean approved) {
        return convertor.convert(bookingService.approvedOrRejectedBooking(userId, bookingId, approved));
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getInfoBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long bookingId) {
        return convertor.convert(bookingService.getInfoBooking(userId, bookingId));
    }

    @GetMapping
    public List<BookingResponseDto> getListUserBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                                       @RequestParam(name = "state", required = false, defaultValue = "ALL") String state) {
        return convertor.getListResponse(bookingService.getListUserBooking(userId, state));
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getUserListBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                                       @RequestParam(name = "state", required = false, defaultValue = "ALL") String state) {
        return convertor.getListResponse(bookingService.getUserListBooking(userId, state));
    }
}
