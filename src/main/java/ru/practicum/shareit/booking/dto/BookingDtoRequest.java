package ru.practicum.shareit.booking.dto;

import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@BookingValid
@EqualsAndHashCode
@ToString
@Accessors(chain = true)
public class BookingDtoRequest {
    private Long itemId;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;
}
