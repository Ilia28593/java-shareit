package ru.practicum.shareit.user.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Accessors(chain = true)
public class UserDto {
    private Long id;
    private String name;
    private String email;
}

