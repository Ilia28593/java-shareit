package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.config.Create;
import ru.practicum.shareit.config.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotNull
    private String name;
    @NotEmpty(message = "email can not be empty", groups = {Create.class})
    @Email(message = "email must match pattern", groups = {Create.class, Update.class})
    @NotNull
    private String email;
}

