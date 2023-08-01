package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class UserRequestDto {
    protected String name;
    @Email
    protected String email;
}
