package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static ru.practicum.shareit.config.Constants.USER_NO_FOUND_BY_ID;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(long message) {
        super(USER_NO_FOUND_BY_ID + message);
    }
}
