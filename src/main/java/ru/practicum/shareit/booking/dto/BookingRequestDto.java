package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;


@Data
@Builder(toBuilder = true)
public class BookingRequestDto {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private long item;
    private long booker;
    private BookingStatus bookingStatus;
}
