package ru.practicum.shareit.booking.dto;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Accessors(chain = true)
public class BookingDtoRequest {
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}
