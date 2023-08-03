package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingConvertorBookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingMapperImpl;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

import static ru.practicum.shareit.config.Constants.USER_ID;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingController {

    private final BookingMapperImpl mapper;

    private final BookingConvertorBookingResponseDto convertor;

    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto createBooking(@RequestHeader(USER_ID) long userId,
                                            @RequestBody BookingRequest requestDto) {
        return convertor.convert(bookingService.create(userId, requestDto.getItemId(), mapper.mapToBooking(requestDto)));
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approvedOrRejectedBooking(@RequestHeader(USER_ID) long userId,
                                                        @PathVariable long bookingId,
                                                        @RequestParam(name = "approved") boolean approved) {
        return convertor.convert(bookingService.approvedOrRejectedBooking(userId, bookingId, approved));
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getInfoBooking(@RequestHeader(USER_ID) long userId,
                                             @PathVariable long bookingId) {
        return convertor.convert(bookingService.getInfoBooking(userId, bookingId));
    }

    @GetMapping
    public List<BookingResponseDto> getListUserBooking(@RequestHeader(USER_ID) long userId,
                                                       @RequestParam(name = "state", required = false, defaultValue = "ALL") String state) {
        return convertor.getListResponse(bookingService.getListUserBooking(userId, state));
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getUserListBooking(@RequestHeader(USER_ID) long userId,
                                                       @RequestParam(name = "state", required = false, defaultValue = "ALL") String state) {
        return convertor.getListResponse(bookingService.getUserListBooking(userId, state));
    }
}
