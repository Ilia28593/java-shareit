package ru.practicum.shareit.request.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Builder(toBuilder = true)
public class ItemRequest {
    private long id;
    private String description;
    private User requestor;
    private LocalDateTime created;
}
