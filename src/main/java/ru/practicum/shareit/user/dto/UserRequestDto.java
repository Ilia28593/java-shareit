package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
@Builder(toBuilder = true)
public class UserRequestDto {
    protected String name;
    @Email
    protected String email;
}
