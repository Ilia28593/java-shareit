package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemResponseNameDto;
import ru.practicum.shareit.user.dto.UserIdDto;

import java.time.LocalDateTime;


@Data
@Builder(toBuilder = true)
public class BookingRequestApprovedDto {
    private long id;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime start;
    private String end;
    private ItemResponseNameDto item;
    private UserIdDto booker;
    private BookingStatus status;
}
