package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.dto.UserIdDto;

@Getter
@Setter
@Builder(toBuilder = true)
public class BookingResponseFromGetItems {
    private long id;
    private long bookerId;
}
