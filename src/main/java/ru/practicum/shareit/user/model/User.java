package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.service.Entity;

@Data
@Builder(toBuilder = true)
public class User extends Entity {
    private long id;
    private String name;
    private String email;
}
